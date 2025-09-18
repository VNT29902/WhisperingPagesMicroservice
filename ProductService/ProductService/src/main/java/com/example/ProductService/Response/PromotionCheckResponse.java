package com.example.ProductService.Response;

import java.math.BigDecimal;

public class PromotionCheckResponse  {

    private String productId;
    private BigDecimal discountPercent;   // chỉ % lớn nhất
    private String promotionName;      // optional: tên promotion để client hiển thị
    private String targetType;

    public PromotionCheckResponse() {
    }

    public PromotionCheckResponse(String productId, BigDecimal discountPercent, String promotionName, String targetType) {
        this.productId = productId;
        this.discountPercent = discountPercent;
        this.promotionName = promotionName;
        this.targetType = targetType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
}
