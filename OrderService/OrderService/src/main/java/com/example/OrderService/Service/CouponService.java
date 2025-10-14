package com.example.OrderService.Service;

import com.example.OrderService.Entity.Coupon;
import com.example.OrderService.Entity.CouponRedemption;
import com.example.OrderService.Enum.DiscountType;
import com.example.OrderService.Record.CouponApplicationResult;
import com.example.OrderService.Record.CouponValidationRequest;
import com.example.OrderService.Record.CouponValidationResponse;
import com.example.OrderService.Repository.CouponRedemptionRepository;
import com.example.OrderService.Repository.CouponRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

@Service
public class CouponService {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private final CouponRepository couponRepository;
    private final CouponRedemptionRepository couponRedemptionRepository;

    public CouponService(CouponRepository couponRepository, CouponRedemptionRepository couponRedemptionRepository) {
        this.couponRepository = couponRepository;
        this.couponRedemptionRepository = couponRedemptionRepository;
    }

    @Transactional(readOnly = true)
    public CouponApplicationResult evaluateCoupon(String rawCode, String userName, BigDecimal subtotal) {
        if (rawCode == null || rawCode.isBlank()) {
            return CouponApplicationResult.none();
        }

        BigDecimal normalizedSubtotal = subtotal != null ? subtotal : BigDecimal.ZERO;
        String normalizedCode = rawCode.trim().toUpperCase(Locale.ROOT);
        Coupon coupon = couponRepository.findByCodeIgnoreCase(normalizedCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "INVALID_CODE"));

        validateCouponState(coupon, userName, normalizedSubtotal);

        BigDecimal discountAmount = calculateDiscount(coupon, normalizedSubtotal);
        if (discountAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NO_DISCOUNT_AVAILABLE");
        }
        String message = String.format("Áp dụng mã %s, giảm %s", normalizedCode, discountAmount.setScale(0, ROUNDING_MODE));
        return new CouponApplicationResult(normalizedCode, coupon, discountAmount, message);
    }

    @Transactional(readOnly = true)
    public CouponValidationResponse previewCoupon(String userName, CouponValidationRequest request) {
        String code = request != null ? request.code() : null;
        CouponApplicationResult result = evaluateCoupon(code, userName, request != null ? request.subtotal() : null);
        if (!result.applied()) {
            return new CouponValidationResponse(null, BigDecimal.ZERO, null, null, null, "Không có mã giảm giá được áp dụng");
        }

        return new CouponValidationResponse(
                result.normalizedCode(),
                result.discountAmount(),
                result.coupon().getDiscountType().name(),
                result.coupon().getDiscountValue(),
                result.coupon().getMaxDiscountAmount(),
                result.message()
        );
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void recordRedemption(CouponApplicationResult evaluation, String orderId, String userName) {
        if (evaluation == null || !evaluation.applied()) {
            return;
        }
        Coupon coupon = evaluation.coupon();
        Objects.requireNonNull(coupon, "Coupon must not be null when recording redemption");

        // Double-check limits inside the same transaction to avoid race conditions.
        long totalRedeemed = couponRedemptionRepository.countByCoupon_Id(coupon.getId());
        if (coupon.getMaxUses() != null && totalRedeemed >= coupon.getMaxUses()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "MAX_REDEMPTIONS_REACHED");
        }

        long userRedeemed = couponRedemptionRepository.countByCoupon_IdAndUserNameIgnoreCase(coupon.getId(), userName);
        if (coupon.getMaxUsesPerCustomer() != null && userRedeemed >= coupon.getMaxUsesPerCustomer()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CUSTOMER_USAGE_EXCEEDED");
        }

        if (couponRedemptionRepository.existsByCoupon_IdAndOrderId(coupon.getId(), orderId)) {
            return;
        }

        CouponRedemption redemption = new CouponRedemption();
        redemption.setCoupon(coupon);
        redemption.setOrderId(orderId);
        redemption.setUserName(userName);
        redemption.setDiscountAmount(evaluation.discountAmount());
        redemption.setRedeemedAt(LocalDateTime.now());
        couponRedemptionRepository.save(redemption);
    }

    private void validateCouponState(Coupon coupon, String userName, BigDecimal subtotal) {
        if (!coupon.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "COUPON_INACTIVE");
        }
        LocalDateTime now = LocalDateTime.now();
        if (coupon.getStartAt() != null && now.isBefore(coupon.getStartAt())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "COUPON_NOT_STARTED");
        }
        if (coupon.getEndAt() != null && now.isAfter(coupon.getEndAt())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "COUPON_EXPIRED");
        }

        long totalRedeemed = couponRedemptionRepository.countByCoupon_Id(coupon.getId());
        if (coupon.getMaxUses() != null && totalRedeemed >= coupon.getMaxUses()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MAX_REDEMPTIONS_REACHED");
        }

        long userRedeemed = couponRedemptionRepository.countByCoupon_IdAndUserNameIgnoreCase(coupon.getId(), userName);
        if (coupon.getMaxUsesPerCustomer() != null && userRedeemed >= coupon.getMaxUsesPerCustomer()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CUSTOMER_USAGE_EXCEEDED");
        }

        if (coupon.getMinSubtotal() != null && subtotal.compareTo(coupon.getMinSubtotal()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MIN_SUBTOTAL_NOT_MET");
        }
    }

    private BigDecimal calculateDiscount(Coupon coupon, BigDecimal subtotal) {
        BigDecimal discount;
        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            discount = subtotal.multiply(coupon.getDiscountValue()).divide(ONE_HUNDRED, 2, ROUNDING_MODE);
            if (coupon.getMaxDiscountAmount() != null) {
                discount = discount.min(coupon.getMaxDiscountAmount());
            }
        } else {
            discount = coupon.getDiscountValue();
        }

        discount = discount.min(subtotal).max(BigDecimal.ZERO);
        return discount.setScale(2, ROUNDING_MODE);
    }
}
