package com.example.OrderService.Controller;

import com.example.OrderService.Record.CouponValidationRequest;
import com.example.OrderService.Record.CouponValidationResponse;
import com.example.OrderService.Service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/validate")
    public ResponseEntity<CouponValidationResponse> validateCoupon(
            @RequestHeader("X-User-Name") String userName,
            @RequestBody CouponValidationRequest request
    ) {
        return ResponseEntity.ok(couponService.previewCoupon(userName, request));
    }
}
