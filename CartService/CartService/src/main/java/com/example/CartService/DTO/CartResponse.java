package com.example.CartService.DTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CartResponse {
    private String cartId;
    private List<CartItemDTO> items;
    private BigDecimal totalAmount;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}

