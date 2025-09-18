package com.example.CartService.Service;

import com.example.CartService.DTO.CartItemDTO;
import com.example.CartService.DTO.CartResponse;
import com.example.CartService.Entity.Cart;
import com.example.CartService.Entity.CartItem;
import com.example.CartService.FeignClient.OrdersClient;
import com.example.CartService.FeignClient.ProductClient;
import com.example.CartService.Repository.CartItemRepository;
import com.example.CartService.Repository.CartRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrdersClient ordersClient;

    String id = "CAR-" + RandomStringUtils.randomAlphanumeric(8).toUpperCase();


    public CartResponse getCartByUserName(String userName) {
        Cart cart = cartRepository.findByUserName(userName)
                .orElseGet(() -> createNewCart(userName));

        List<CartItemDTO> items = cart.getItems().stream()
                .map(item -> new CartItemDTO(
                        item.getProductId(),
                        item.getTitle(),
                        item.getImage(),
                        item.getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        BigDecimal totalPrice = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CartResponse response = new CartResponse();
        response.setCartId(cart.getId());
        response.setItems(items);
        response.setTotalAmount(totalPrice);
        return response;
    }


    @Transactional
    public void addToCart(String userName, CartItemDTO itemRequest) {




        ResponseEntity<String> checkStock = productClient.checkStock(itemRequest.getProductId(), itemRequest.getQuantity());

        if (!checkStock.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("❌ Stock not enough for product: " + itemRequest.getProductId());
        }


        Cart cart = cartRepository.findByUserName(userName)
                .orElseGet(() -> createNewCart(userName));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(itemRequest.getProductId()))
                .findFirst();


        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + itemRequest.getQuantity());
        } else {

            BigDecimal productPrice = fetchProductPrice(itemRequest.getProductId());

            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProductId(itemRequest.getProductId());
            item.setImage(itemRequest.getImage());
            item.setTitle(itemRequest.getTitle());
            item.setPrice(itemRequest.getPrice());
            item.setQuantity(itemRequest.getQuantity());
            item.setAddedAt(LocalDateTime.now());
            cart.getItems().add(item);


        }

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }


    @Transactional
    public void updateItemQuantity(String userName, String productId, int quantity) {
        Cart cart = cartRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        if (quantity <= 0) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
        }

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }


    @Transactional
    public void removeItem(String userName, String productId) {
        updateItemQuantity(userName, productId, 0);
    }


    @Transactional
    public void clearCart(String userName) {
        Cart cart = cartRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }


    @Transactional(readOnly = true)
    public void validateCartItems(String userName, List<CartItemDTO> cartItemDTO) {
        Cart cart = cartRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("❌ Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("❌ Cart is empty");
        }

        List<CartItem> actualCartItems = cartItemRepository.findByCart(cart);

        Map<String, Integer> actualQuantityMap = actualCartItems.stream()
                .collect(Collectors.toMap(CartItem::getProductId, CartItem::getQuantity));

        for (CartItemDTO item : cartItemDTO) {
            String productId = item.getProductId();
            int requestedQuantity = item.getQuantity();

            int actualQuantity = actualQuantityMap.getOrDefault(productId, 0);

            if (requestedQuantity > actualQuantity) {
                throw new IllegalStateException("❌ Quantity exceeded for productId: " + productId
                        + " | Requested: " + requestedQuantity
                        + " | In cart: " + actualQuantity);
            }

            // Gọi sang ProductService để kiểm tra tồn kho
            ResponseEntity<String> stockResponse = productClient.checkStock(productId, requestedQuantity);
            if (!stockResponse.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("❌ Product out of stock or not found: " + productId);
            }
        }
    }

    @Transactional
    public void processOrderAndClearCart(String userName, List<CartItemDTO> cartItemDTO) {

        // Gọi Product Service để trừ hàng
        for (CartItemDTO item : cartItemDTO) {
            ResponseEntity<String> stockResponse = productClient.placeOrder(item.getProductId(), item.getQuantity());

            if (!stockResponse.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("❌ Failed to reduce stock for productId: " + item.getProductId());
            }
        }

        // Xóa item đã mua khỏi cart
        try {
            deleteCartItemsByProductIdList(userName, cartItemDTO);
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to delete cart items after order creation", e);
        }
    }

    private Cart createNewCart(String userName) {
        Cart cart = new Cart();
        cart.setId(id);
        cart.setUserName(userName);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    private BigDecimal fetchProductPrice(String productId) {
        BigDecimal price = productClient.getPriceProduct(productId);
        if (price == null) {
            throw new IllegalStateException("Not found price of product " + productId);
        }
        return price;
    }

    @Transactional
    public void deleteCartItemsByProductIdList(String userName, List<CartItemDTO> items) {
        Cart cart = cartRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("❌ Cart not found for user: " + userName));

        List<String> productIds = items.stream()
                .map(CartItemDTO::getProductId)
                .collect(Collectors.toList());

        cartRepository.deleteByCartIdAndProductIds(cart.getId(), productIds);
    }

}

