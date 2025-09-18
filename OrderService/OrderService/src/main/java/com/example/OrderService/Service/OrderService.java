package com.example.OrderService.Service;

import com.example.OrderService.Client.CartClient;
import com.example.OrderService.Client.ProductClient;
import com.example.OrderService.DTO.OrderItemDTO;
import com.example.OrderService.Entity.Order;
import com.example.OrderService.Entity.OrderItem;
import com.example.OrderService.Enum.OrderStatus;
import com.example.OrderService.Enum.PaymentMethod;
import com.example.OrderService.Mapper.OrderItemMapper;
import com.example.OrderService.Record.CreateOrderRequest;
import com.example.OrderService.Record.OrderWithItemsResponse;
import com.example.OrderService.Repository.OrderItemRepository;
import com.example.OrderService.Repository.OrderRepository;
import com.example.OrderService.Response.OrderItemResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CONFLICT;

@Service
@Slf4j
public class OrderService {

    private static final int PAYMENT_TTL_MINUTES = 3;

    private static final BigDecimal SHIPPING_FEE = BigDecimal.valueOf(40000);

    private  final ShippingInfoService shippingInfoService;

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductClient productClient;

    private final CartClient cartClient;

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(OrderService.class);


    public OrderService(ShippingInfoService shippingInfoService, OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductClient productClient, CartClient cartClient) {
        this.shippingInfoService = shippingInfoService;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productClient = productClient;
        this.cartClient = cartClient;

    }

    @Transactional
    public Order createOrder(String userName, CreateOrderRequest req) {

        if (req == null || req.items() == null || req.items().isEmpty()) {
            throw new IllegalArgumentException("Danh sách sản phẩm trống");
        }
        final PaymentMethod method = Optional.ofNullable(req.paymentMethod())
                .orElse(PaymentMethod.COD);


        List<OrderItem> orderItems = OrderItemMapper.toEntityList(req.items());


        BigDecimal totalAmount = calculateTotalAmount(orderItems);


        Order order = buildOrder(userName, method, totalAmount);


        order.addItems(orderItems);
        order = orderRepository.save(order);

        shippingInfoService.addShippingInfo(order.getId(), req.shippingAddress());


        try {
            if (isOnline(method)) {

                reserveStock(order.getId(), orderItems);
                order.setStatus(OrderStatus.PENDING_PAYMENT);
                orderRepository.save(order);
            } else {
                // COD: confirm (trừ DB) rồi set CONFIRMED
                confirmStock(order.getId(), orderItems);
                order.setStatus(OrderStatus.CONFIRMED);
                orderRepository.save(order);
            }
            // delete item in cart
            try {
                deleteCartItem(userName, req.items());
            } catch (Exception e) {
                log.error("⚠ Failed to delete cart items for user {}", userName, e);

            }


        } catch (Exception ex) {

            throw ex;
        }

        return order;
    }


    public OrderWithItemsResponse getOrderWithItemsById(String orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    List<OrderItemResponse> items = orderItemRepository.findByOrder_Id(order.getId())
                            .stream()
                            .map(OrderItemMapper::toResponse)
                            .toList();

                    return new OrderWithItemsResponse(
                            order.getId(),
                            order.getStatus(),
                            order.getUserName(),
                            order.getCreatedAt(),
                            order.getTotalAmount(),
                            items
                    );
                })
                .orElse(null);
    }


    public Page<OrderWithItemsResponse> getAllOrdersWithItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<OrderWithItemsResponse> responses = orderPage.getContent().stream()
                .map(order -> {
                    List<OrderItemResponse> items = orderItemRepository.findByOrder_Id(order.getId())
                            .stream()
                            .map(OrderItemMapper::toResponse)
                            .toList();

                    return new OrderWithItemsResponse(
                            order.getId(),
                            order.getStatus(),
                            order.getUserName(),
                            order.getCreatedAt(),
                            order.getTotalAmount(),
                            items
                    );
                })
                .toList();

        return new PageImpl<>(responses, pageable, orderPage.getTotalElements());
    }



    private boolean isOnline(PaymentMethod paymentMethod) {
        return paymentMethod == PaymentMethod.MOMO;
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> items) {
        BigDecimal totalItemsPrice = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalItemsPrice.add(SHIPPING_FEE);
    }


    private Order buildOrder(String userName, PaymentMethod paymentMethod, BigDecimal totalAmount) {
        String id = "ORD-" + RandomStringUtils.randomAlphanumeric(8).toUpperCase();

        Order order = new Order();
        order.setId(id);
        order.setUserName(userName);
        order.setPaymentMethod(paymentMethod);
        order.setTotalAmount(totalAmount);
        order.setCreatedAt(LocalDateTime.now());

        if (isOnline(paymentMethod)) {
            order.setStatus(OrderStatus.PENDING_PAYMENT);
            order.setExpiresAt(LocalDateTime.now().plusMinutes(PAYMENT_TTL_MINUTES));
        } else {
            // Đừng set CONFIRMED trước khi thật sự confirm stock
            order.setStatus(OrderStatus.CONFIRMED);
            order.setExpiresAt(null);
        }

        return order;
    }


    /** Giữ chỗ tồn kho (reserve) cho thanh toán online trong 3 phút */
    private void reserveStock(String orderId, List<OrderItem> items) {
        // Gợi ý: triển khai 1 endpoint batch ở Product/Inventory Service để reserve theo orderId
        // Tạm thời gọi từng item (tuỳ API hiện tại của bạn):
        for (OrderItem item : items) {
            ResponseEntity<String> res = productClient.reserveStock(item.getProductId(), item.getQuantity(), PAYMENT_TTL_MINUTES * 60);
            if (!res.getStatusCode().is2xxSuccessful()) {
                throw new ResponseStatusException(CONFLICT, "OUT_OF_STOCK: " + res.getBody());
            }
        }
    }

    /** Trừ tồn thật (confirm) cho COD hoặc callback thanh toán thành công */
    private void confirmStock(String orderId, List<OrderItem> items) {
        // Nếu ProductClient chưa có confirm theo orderId, tạm thời gọi theo từng product
        for (OrderItem item : items) {
            ResponseEntity<String> res = productClient.confirmStock(item.getProductId(), item.getQuantity());
            if (!res.getStatusCode().is2xxSuccessful()) {
                throw new ResponseStatusException(CONFLICT, "CONFIRM_STOCK_FAILED: " + res.getBody());
            }
        }
    }

    private void deleteCartItem(String userName, List<OrderItemDTO> items) {
        List<String> failedProducts = new ArrayList<>();

        for (OrderItemDTO item : items) {
            try {
                cartClient.removeItem(item.getProductId(), userName);
            } catch (Exception ex) {
                log.error("❌ Failed to remove product {} from cart for user {}", item.getProductId(), userName, ex);
                failedProducts.add(item.getProductId());
            }
        }

        if (!failedProducts.isEmpty()) {
            log.warn("⚠ Some items could not be removed from cart: {}", failedProducts);

        }
    }




    /** Public API để Payment Service callback khi thanh toán thành công */
    @Transactional
    public void markOrderAsPaidAndConfirm(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (order.getStatus() == OrderStatus.CONFIRMED || order.getStatus() == OrderStatus.PAID) {
            return; // idempotent
        }

        // Confirm stock (nếu đang PENDING_PAYMENT)
        if (order.getStatus() == OrderStatus.PENDING_PAYMENT) {
            confirmStock(orderId, order.getItems());
        }
        order.setStatus(OrderStatus.PAID); // hoặc CONFIRMED tuỳ định nghĩa
        orderRepository.save(order);
    }

    /** Job: hết 3 phút mà chưa thanh toán thì hủy & release stock */
    @Transactional
    @Scheduled(fixedDelay = 30_000) // quét mỗi 30s
    public void processExpiredOrders() {
        List<Order> expired = orderRepository.findByStatusAndExpiresAtBefore(OrderStatus.PENDING_PAYMENT, LocalDateTime.now());
        for (Order order : expired) {
            try {
                // Release stock đã reserve
                for (OrderItem item : order.getItems()) {
                    productClient.releaseStock(item.getProductId(), item.getQuantity());
                }
                order.setStatus(OrderStatus.CANCELLED);
                order.setExpiresAt(null);
                orderRepository.save(order);
                log.info("Expired & canceled order {}", order.getId());
            } catch (Exception ex) {
                log.error("Failed to expire order {}: {}", order.getId(), ex.getMessage(), ex);
            }
        }
    }

    // Giữ nguyên các hàm nhóm đơn, delete... nếu bạn cần
    public Map<String, List<OrderItemResponse>> getGroupedOrdersByUserName(String userName) {
        List<Order> orderList = orderRepository.findByUserName(userName);
        if (orderList == null || orderList.isEmpty()) return Map.of();
        return orderList.stream()
                .map(o -> orderItemRepository.findByOrder_Id(o.getId()))
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(OrderItemMapper::toResponse)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(OrderItemResponse::getOrderId));
    }

    public List<OrderWithItemsResponse> getOrdersWithItemsByUserName(String userName) {
        List<Order> orderList = orderRepository.findByUserName(userName);

        if (orderList == null || orderList.isEmpty()) {
            return List.of();
        }

        return orderList.stream()
                .map(order -> {
                    List<OrderItemResponse> items = orderItemRepository.findByOrder_Id(order.getId())
                            .stream()
                            .map(OrderItemMapper::toResponse)
                            .filter(Objects::nonNull)
                            .toList();

                    return new OrderWithItemsResponse(
                            order.getId(),
                            order.getStatus(),
                            order.getUserName(),
                            order.getCreatedAt(),
                            order.getTotalAmount(),
                            items
                    );
                })
                .toList();
    }




    public void deleteOrdersByUserName(String userName) {
        orderRepository.deleteByUserName(userName);
    }

    public void markOrderAsPaid(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }

    @Transactional
    public boolean markAsDelivered(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return false;

        if (order.getStatus() != OrderStatus.CONFIRMED && order.getStatus() != OrderStatus.PAID) {
            return false; // trạng thái không hợp lệ để chuyển
        }

        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
        return true;
    }

}
