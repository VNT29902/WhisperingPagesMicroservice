package com.example.ProductService.BookDto;

public class PromotionCheckRequest {

    private String productId;
    private String category;


    public PromotionCheckRequest() {
    }

    public PromotionCheckRequest(String productId, String category) {
        this.productId = productId;
        this.category = category;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
