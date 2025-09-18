package com.example.OrderService.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderItemResponse {
    private String id;
    private String productId;
    private String title;
    private String image;
    private int quantity;
    private BigDecimal price;
    private LocalDateTime addedAt;

    private String orderId;

    public OrderItemResponse(String id, String productId, String title, String image, int quantity, BigDecimal price, LocalDateTime addedAt, String orderId) {
        this.id = id;
        this.productId = productId;
        this.title = title;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.addedAt = addedAt;
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}
