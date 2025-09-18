package com.example.OrderService.Controller;


import com.example.OrderService.DTO.OrderItemDTO;
import com.example.OrderService.DTO.ShippingAddressDTO;
import com.example.OrderService.Record.CreateOrderRequest;
import com.example.OrderService.Record.OrderWithItemsResponse;
import com.example.OrderService.Response.OrderItemResponse;
import com.example.OrderService.Response.OrderResponse;
import com.example.OrderService.Response.ShippingInfoResponse;
import com.example.OrderService.Entity.Order;
import com.example.OrderService.Entity.ShippingInfo;
import com.example.OrderService.Service.OrderService;
import com.example.OrderService.Service.ShippingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/orders")
public class OrderController {


    @Autowired
    private  OrderService orderService;


    @Autowired
    private ShippingInfoService shippingInfoService;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestHeader("X-User-Name") String userName,
            @RequestBody CreateOrderRequest createOrderRequest
    ) {
        try {
            Order order = orderService.createOrder(userName, createOrderRequest);
            OrderResponse response = new OrderResponse(
                    order.getId(),
                    order.getTotalAmount(),
                    order.getStatus(),
                    order.getCreatedAt()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to create order");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Trong OrderController
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderWithItemsResponse> getOrderWithItemsById(
            @PathVariable String orderId
    ) {
        OrderWithItemsResponse order = orderService.getOrderWithItemsById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }





    @GetMapping
    public ResponseEntity<List<OrderWithItemsResponse>> getOrdersWithItems(
            @RequestHeader("X-User-Name") String userName
    ) {
        List<OrderWithItemsResponse> orders = orderService.getOrdersWithItemsByUserName(userName);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<OrderWithItemsResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(orderService.getAllOrdersWithItems(page, size));
    }


    @PutMapping("/{orderId}/delivered")
    public ResponseEntity<String> markOrderAsDelivered(@PathVariable String orderId) {
        boolean updated = orderService.markAsDelivered(orderId);
        if (updated) {
            return ResponseEntity.ok("✅ Order marked as DELIVERED");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ Cannot update order to DELIVERED");
        }
    }





    @DeleteMapping
    public ResponseEntity<String> deleteOrdersByUserName(@RequestParam String userName) {
        orderService.deleteOrdersByUserName(userName);
        return ResponseEntity.ok("🗑️ Deleted orders for user: " + userName);
    }



    @PostMapping("/{orderId}/mark-paid")
    public ResponseEntity<Void> markOrderAsPaid(@PathVariable String orderId) {
        orderService.markOrderAsPaid(orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/shipping")
    public ResponseEntity<?> addShippingInfo(@RequestParam String orderId,
                                             @RequestBody ShippingAddressDTO shippingRequest) {
        try {
            ShippingInfoResponse saved = shippingInfoService.addShippingInfo(orderId, shippingRequest);
            return ResponseEntity.ok(saved);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/shipping/{orderId}")
    public ResponseEntity<?> geShippingByOrderId(@PathVariable String orderId) {
        try {
            ShippingInfo info = shippingInfoService.getByOrderId(orderId);
            return ResponseEntity.ok(info);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{orderId}/shipping-info")
    public ResponseEntity<ShippingInfoResponse> getShippingInfo(@PathVariable String orderId) {
        return ResponseEntity.ok(shippingInfoService.getShippingInfoByOrderId(orderId));
    }


}
