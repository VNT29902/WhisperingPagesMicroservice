package com.example.ProductService.BookDto;

import com.example.ProductService.Entity.Promotion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PromotionSaveRequest {

    private String name;
    private Promotion.TargetType targetType;     // GLOBAL hoặc CATEGORY
    private String targetValue;    // null nếu GLOBAL, category slug nếu CATEGORY
    private Promotion.DiscountType discountType;

    private BigDecimal discountValue;
    private LocalDate startDate;
    private LocalDate endDate;


    public PromotionSaveRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
