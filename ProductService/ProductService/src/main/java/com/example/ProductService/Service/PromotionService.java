package com.example.ProductService.Service;

import com.example.ProductService.BookDto.PromotionCheckRequest;
import com.example.ProductService.BookDto.PromotionSaveRequest;
import com.example.ProductService.BookDto.PromotionUpdateRequest;
import com.example.ProductService.Entity.Promotion;
import com.example.ProductService.Enum.ErrorCode;
import com.example.ProductService.Exception.PromotionException;
import com.example.ProductService.Exception.ResourceNotFoundException;
import com.example.ProductService.Mapper.PromotionMapper;
import com.example.ProductService.Repository.PromotionRepository;
import com.example.ProductService.Response.PromotionCheckResponse;
import com.example.ProductService.Response.PromotionListResponse;
import com.example.ProductService.Response.PromotionSaveResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public PromotionCheckResponse getBestPromotionForProduct(PromotionCheckRequest request) {
        List<Promotion> promos = promotionRepository.findApplicablePromotions(request.getCategory());

        Promotion bestPromo = promos.stream().filter(p -> p.getDiscountType() == Promotion.DiscountType.PERCENT).max(Comparator.comparing(Promotion::getDiscountPercent)).orElse(null);

        if (bestPromo == null) {
            return new PromotionCheckResponse(request.getProductId(), BigDecimal.ZERO, null, null);
        }


        return new PromotionCheckResponse(request.getProductId(), bestPromo.getDiscountPercent(), bestPromo.getName(), bestPromo.getTargetType() != null ? bestPromo.getTargetType().name() : null);
    }


    public PromotionSaveResponse getPromotionById(Long id) {
        Promotion promo = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with id: " + id));

        return PromotionMapper.toSaveResponse(promo);    }

    public PromotionSaveResponse savePromotion(PromotionSaveRequest req) {

        // 1. Validate tên
        if (req.getName() == null || req.getName().isBlank()) {
            throw new PromotionException("Promotion name cannot be empty");
        }

        // 2. Validate loại khuyến mãi (GLOBAL hoặc CATEGORY)
        if (req.getTargetType() == null
                || (req.getTargetType() != Promotion.TargetType.GLOBAL
                && req.getTargetType() != Promotion.TargetType.CATEGORY)) {
            throw new PromotionException("Target type must be GLOBAL or CATEGORY");
        }

        // 3. Validate targetValue (nếu là CATEGORY thì bắt buộc có)
        if (req.getTargetType() == Promotion.TargetType.CATEGORY &&
                (req.getTargetValue() == null)) {
            throw new PromotionException("Target value is required when target type is CATEGORY");
        }

        // 4. Validate discount
        if (req.getDiscountType() == null) {
            throw new PromotionException("Discount type cannot be null");
        }
        if (req.getDiscountValue() == null || req.getDiscountValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PromotionException("Discount value must be greater than 0");
        }
        // Nếu là PERCENT thì giới hạn 1–100
        if (req.getDiscountType() == Promotion.DiscountType.PERCENT &&
                req.getDiscountValue().compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new PromotionException("Percent discount must be between 1 and 100");
        }

        // 5. Validate ngày tháng
        if (req.getStartDate() == null || req.getEndDate() == null) {
            throw new PromotionException("Start date and end date cannot be null");
        }
        if (req.getEndDate().isBefore(req.getStartDate())) {
            throw new PromotionException("End date must be after start date");
        }

        // --- Mapping ---
        Promotion promotion = PromotionMapper.toEntity(req);

        // Save vào DB
        Promotion saved = promotionRepository.save(promotion);

        return PromotionMapper.toResponse(saved);
    }


    public void deletePromotionsById(Long id) {
        String resourceType = "promotions";
        try {
            promotionRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException(ErrorCode.PROMOTION_NOT_FOUND, id);

        }

    }

    public void updatePromotionStatus(Long id, Boolean isActive) {
        Promotion promotion = promotionRepository.findById(id).orElseThrow(() -> new PromotionException("Promotion with id " + id + " not found"));

        promotion.setIsActive(isActive);
        promotionRepository.save(promotion);
    }


    public Page<PromotionListResponse> getPromotions(int page, int size, Boolean active) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Promotion> promotions;

        promotions = promotionRepository.findAll(pageable); // tất cả

        return promotions.map(PromotionMapper::toListResponse);
    }


    public PromotionSaveResponse updatePromotion(Long id, PromotionUpdateRequest req) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new PromotionException("Promotion with id " + id + " not found"));

        if (req.getName() != null) {
            promotion.setName(req.getName());
        }
        if (req.getDiscountType() != null) {
            promotion.setDiscountType(req.getDiscountType());
        }
        if (req.getDiscountValue() != null) {
            promotion.setDiscountValue(req.getDiscountValue());
        }
        if (req.getTargetType() != null) {
            promotion.setTargetType(req.getTargetType());
        }
        if (req.getTargetValue() != null) {
            promotion.setTargetValue(req.getTargetValue());
        }
        if (req.getStartDate() != null) {
            promotion.setStartDate(req.getStartDate());
        }
        if (req.getEndDate() != null) {
            promotion.setEndDate(req.getEndDate());
        }
        if (req.getActive() != null) {
            promotion.setIsActive(req.getActive());
        }

        Promotion saved = promotionRepository.save(promotion);
        return PromotionMapper.toResponse(saved);
    }



}
