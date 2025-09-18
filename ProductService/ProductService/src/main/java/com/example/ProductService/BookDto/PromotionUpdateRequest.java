package com.example.ProductService.BookDto;

import com.example.ProductService.Entity.Promotion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PromotionUpdateRequest {

    private String name;

    private Promotion.DiscountType discountType; // ENUM PERCENT hoặc FIXED
    private BigDecimal discountValue; // Giá trị giảm

    private Promotion.TargetType targetType;  // ENUM CATEGORY hoặc PRODUCT
    private String targetValue; // Ví dụ: "khoa-hoc", "lich-su"

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean active;

    public PromotionUpdateRequest() {
    }

    public PromotionUpdateRequest(String name, Promotion.DiscountType discountType, BigDecimal discountValue, Promotion.TargetType targetType, String targetValue, LocalDate startDate, LocalDate endDate, Boolean active) {
        this.name = name;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.targetType = targetType;
        this.targetValue = targetValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Promotion.DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Promotion.DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public Promotion.TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(Promotion.TargetType targetType) {
        this.targetType = targetType;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
