package com.example.OrderService.Record;

import java.math.BigDecimal;

public record CouponValidationRequest(
        String code,
        BigDecimal subtotal
) {}
