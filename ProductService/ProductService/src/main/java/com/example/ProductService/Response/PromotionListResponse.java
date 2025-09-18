package com.example.ProductService.Response;

import com.example.ProductService.Entity.Promotion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PromotionListResponse {

    private Long id;
    private String name;
    private Promotion.DiscountType discountType;
    private BigDecimal discountValue;
    private Promotion.TargetType targetType;
    private String targetValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;

    public PromotionListResponse() {
    }


    public PromotionListResponse(Long id, String name, Promotion.DiscountType discountType, BigDecimal discountValue, Promotion.TargetType targetType, String targetValue, LocalDate startDate, LocalDate endDate, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.targetType = targetType;
        this.targetValue = targetValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
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
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
