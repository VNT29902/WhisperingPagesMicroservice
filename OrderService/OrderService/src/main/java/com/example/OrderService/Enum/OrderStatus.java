package com.example.OrderService.Enum;

public enum OrderStatus {
    PENDING_PAYMENT, // online: chờ thanh toán
    CONFIRMED,       // đã xác nhận (COD hoặc online thành công)
    PAID,            // đã thanh toán online
    CANCELLED,

    DELIVERED
    // đã hủy
}
