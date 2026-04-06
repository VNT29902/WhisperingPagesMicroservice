package com.example.OrderService.Record;

import com.example.OrderService.Entity.Coupon;

import java.math.BigDecimal;

public record CouponApplicationResult(
        String normalizedCode,
        Coupon coupon,
        BigDecimal discountAmount,
        String message
) {
    public static CouponApplicationResult none() {
        return new CouponApplicationResult(null, null, BigDecimal.ZERO, "");
    }

    public boolean applied() {
        return coupon != null;
    }
}
