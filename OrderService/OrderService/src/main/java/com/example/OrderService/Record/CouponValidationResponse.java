package com.example.OrderService.Record;

import java.math.BigDecimal;

public record CouponValidationResponse(
        String code,
        BigDecimal discountAmount,
        String discountType,
        BigDecimal discountValue,
        BigDecimal maxDiscountAmount,
        String message
) {}
