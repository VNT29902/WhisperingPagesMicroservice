package com.example.CartService.Controller;


import com.example.CartService.DTO.CartItemDTO;
import com.example.CartService.DTO.CartResponse;
import com.example.CartService.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;


    @GetMapping()
    public ResponseEntity<CartResponse> getMyCart(@RequestHeader("X-User-Name") String userName) {
        CartResponse cart = cartService.getCartByUserName(userName);
        return ResponseEntity.ok(cart);
    }


    @PostMapping("/items")
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-Name") String userName,
                                            @RequestBody CartItemDTO cartItemDTO) {
        cartService.addToCart(userName, cartItemDTO);
        return ResponseEntity.ok("✅ Item added to cart");
    }


    @PutMapping("/item/{productId}")
    public ResponseEntity<?> updateItemQuantity(@PathVariable String productId,
                                                @RequestParam int quantity, @RequestHeader("X-User-Name") String userName) {
        cartService.updateItemQuantity(userName, productId, quantity);
        return ResponseEntity.ok("✅ Quantity updated");
    }


    @DeleteMapping("/item/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable String productId,
                                           @RequestHeader("X-User-Name") String userName) {
        cartService.removeItem(userName, productId);
        return ResponseEntity.noContent().build(); // HTTP 204
    }



    @DeleteMapping
    public ResponseEntity<?> clearCart( @RequestHeader("X-User-Name") String userName) {
        cartService.clearCart(userName);
        return ResponseEntity.ok("🗑️ Cart cleared");
    }


    @PostMapping("/validate")
    public ResponseEntity<String> validateCart(@RequestParam String userName,
                                               @RequestBody List<CartItemDTO> cartItems) {
        try {
            cartService.validateCartItems(userName, cartItems);
            return ResponseEntity.ok("✅ Cart items are valid and in stock");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Validation failed: " + e.getMessage());
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestParam String userName,
                                           @RequestBody List<CartItemDTO> cartItems) {
        try {
            cartService.processOrderAndClearCart(userName, cartItems);
            return ResponseEntity.ok("✅ Order created, stock updated and cart cleared");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Checkout failed: " + e.getMessage());
        }
    }


    @DeleteMapping("/{userName}/items")
    public ResponseEntity<Void> deleteOrderItems(@PathVariable String userName,
                                                 @RequestBody List<CartItemDTO> items) {
        cartService.deleteCartItemsByProductIdList(userName, items);
        return ResponseEntity.ok().build();
    }
}

