package com.example.OrderService.Repository;

import com.example.OrderService.Entity.CouponRedemption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRedemptionRepository extends JpaRepository<CouponRedemption, String> {
    long countByCoupon_Id(String couponId);

    long countByCoupon_IdAndUserNameIgnoreCase(String couponId, String userName);

    boolean existsByCoupon_IdAndOrderId(String couponId, String orderId);
}
