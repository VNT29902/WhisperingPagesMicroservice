package com.example.OrderService.Mapper;

import com.example.OrderService.DTO.OrderItemDTO;
import com.example.OrderService.Entity.OrderItem;
import com.example.OrderService.Response.OrderItemResponse;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderItemMapper {


    public static OrderItem toEntity(OrderItemDTO dto) {
        String id = "ORD-" + RandomStringUtils.randomAlphanumeric(8).toUpperCase();
        OrderItem item = new OrderItem();
        item.setId(id); // tự tạo ID
        item.setProductId(dto.getProductId());
        item.setTitle(dto.getTitle());
        item.setImage(dto.getImage());
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());
        item.setAddedAt(LocalDateTime.now()); // hoặc truyền từ ngoài nếu cần
        return item;
    }

    public static List<OrderItem> toEntityList(List<OrderItemDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) return List.of();
        return dtoList.stream()
                .map(OrderItemMapper::toEntity)
                .collect(Collectors.toList());
    }

    public static OrderItemResponse toResponse(OrderItem item) {
        if (item == null) return null;

        return new OrderItemResponse(
                item.getId(),
                item.getProductId(),
                item.getTitle(),
                item.getImage(),
                item.getQuantity(),
                item.getPrice(),
                item.getAddedAt(),
                item.getOrder() != null ? item.getOrder().getId() : null

        );
    }



    public static List<OrderItemResponse> toResponseList(List<OrderItem> items) {
        if (items == null || items.isEmpty()) return List.of();
        return items.stream()
                .map(OrderItemMapper::toResponse)
                .collect(Collectors.toList());
    }
}
