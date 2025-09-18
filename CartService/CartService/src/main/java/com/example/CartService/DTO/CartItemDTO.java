package com.example.CartService.DTO;

import java.math.BigDecimal;
import java.util.UUID;

public class CartItemDTO {
    private String productId;
    private String title;
    private String image;
    private BigDecimal price;
    private int quantity;

    public CartItemDTO() {
    }

    public CartItemDTO(String productId, String title, String image, BigDecimal price, int quantity) {
        this.productId = productId;
        this.title = title;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}