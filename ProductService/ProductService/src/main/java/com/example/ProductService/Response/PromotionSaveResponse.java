package com.example.ProductService.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PromotionSaveResponse {

    private Long id;
    private String name;
    private String targetType;
    private String targetValue;
    private BigDecimal discountPercent;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;

    public PromotionSaveResponse() {
    }

    public PromotionSaveResponse(Long id, String name, String targetType, String targetValue, BigDecimal discountPercent, LocalDate startDate, LocalDate endDate, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.targetType = targetType;
        this.targetValue = targetValue;
        this.discountPercent = discountPercent;
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

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
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
