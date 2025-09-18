package com.example.OrderService.Repository;

import com.example.OrderService.Entity.ShippingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShippingInfoRepository extends JpaRepository<ShippingInfo, String> {
    Optional<ShippingInfo> findByOrderId(String orderId);

    boolean existsByOrderId(String orderId);

    Optional<ShippingInfo> findByOrder_Id(String orderId);


}
