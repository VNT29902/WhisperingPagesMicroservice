package com.example.OrderService.Record;

import com.example.OrderService.DTO.OrderItemDTO;
import com.example.OrderService.DTO.ShippingAddressDTO;
import com.example.OrderService.Enum.PaymentMethod;

import java.util.List;

public record CreateOrderRequest(
        PaymentMethod paymentMethod,
        ShippingAddressDTO shippingAddress,
        List<OrderItemDTO> items
) {}
