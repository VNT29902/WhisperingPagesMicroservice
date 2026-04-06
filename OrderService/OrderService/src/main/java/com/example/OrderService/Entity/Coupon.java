package com.example.OrderService.Entity;

import com.example.OrderService.Enum.DiscountType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 20)
    private DiscountType discountType;

    @Column(name = "discount_value", nullable = false, precision = 19, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "min_subtotal", precision = 19, scale = 2)
    private BigDecimal minSubtotal;

    @Column(name = "max_uses")
    private Integer maxUses;

    @Column(name = "max_uses_per_customer")
    private Integer maxUsesPerCustomer;

    @Column(name = "max_discount_amount", precision = 19, scale = 2)
    private BigDecimal maxDiscountAmount;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        if (this.id == null) {
            this.id = "CPN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        normalizeCode();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        normalizeCode();
    }

    private void normalizeCode() {
        if (this.code != null) {
            this.code = this.code.trim().toUpperCase(Locale.ROOT);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        normalizeCode();
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public BigDecimal getMinSubtotal() {
        return minSubtotal;
    }

    public void setMinSubtotal(BigDecimal minSubtotal) {
        this.minSubtotal = minSubtotal;
    }

    public Integer getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }

    public Integer getMaxUsesPerCustomer() {
        return maxUsesPerCustomer;
    }

    public void setMaxUsesPerCustomer(Integer maxUsesPerCustomer) {
        this.maxUsesPerCustomer = maxUsesPerCustomer;
    }

    public BigDecimal getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
