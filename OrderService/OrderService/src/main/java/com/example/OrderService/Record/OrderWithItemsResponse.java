package com.example.OrderService.Record;

import com.example.OrderService.Enum.OrderStatus;
import com.example.OrderService.Response.OrderItemResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderWithItemsResponse(
        String orderId,
        OrderStatus status,

        String userName,
        LocalDateTime createdAt,
        BigDecimal totalAmount,
        List<OrderItemResponse> items
) {}

