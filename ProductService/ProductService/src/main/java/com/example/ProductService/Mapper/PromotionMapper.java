package com.example.ProductService.Mapper;

import com.example.ProductService.Response.PromotionListResponse;
import com.example.ProductService.BookDto.PromotionSaveRequest;
import com.example.ProductService.Entity.Promotion;
import com.example.ProductService.Response.PromotionSaveResponse;

import java.math.BigDecimal;

public class PromotionMapper {

    // Convert Request DTO -> Entity
    public static Promotion toEntity(PromotionSaveRequest req) {
        Promotion promotion = new Promotion();
        promotion.setName(req.getName());
        promotion.setTargetType(req.getTargetType());
        promotion.setTargetValue("GLOBAL".equals(req.getTargetType()) ? null : req.getTargetValue());
        promotion.setDiscountType(Promotion.DiscountType.PERCENT); // hiện tại chỉ dùng %
        promotion.setDiscountValue(req.getDiscountValue());
        promotion.setStartDate(req.getStartDate());
        promotion.setEndDate(req.getEndDate());
        promotion.setIsActive(true);
        return promotion;
    }

    // Convert Entity -> Response DTO
    public static PromotionSaveResponse toResponse(Promotion promotion) {
        return new PromotionSaveResponse(
                promotion.getId(),
                promotion.getName(),
                promotion.getTargetType().name(),
                promotion.getTargetValue(),
                promotion.getDiscountValue(),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.getIsActive()
        );
    }

    public static PromotionListResponse toListResponse(Promotion promotion) {
        return new PromotionListResponse(
                promotion.getId(),
                promotion.getName(),
                promotion.getDiscountType(),
                promotion.getDiscountValue(),
                promotion.getTargetType(),
                promotion.getTargetValue(),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.getIsActive()
        );
    }

    public static PromotionSaveResponse toSaveResponse(Promotion entity) {
        return new PromotionSaveResponse(
                entity.getId(),
                entity.getName(),
                entity.getTargetType().toString(),
                entity.getTargetValue(),
                entity.getDiscountValue(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getIsActive()
        );
    }
}
