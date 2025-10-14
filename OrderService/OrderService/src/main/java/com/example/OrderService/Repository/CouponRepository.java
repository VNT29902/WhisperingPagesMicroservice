package com.example.OrderService.Repository;

import com.example.OrderService.Entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, String> {
    Optional<Coupon> findByCodeIgnoreCase(String code);
}
