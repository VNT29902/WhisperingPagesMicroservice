package com.example.OrderService.Repository;

import com.example.OrderService.Entity.Order;
import com.example.OrderService.Enum.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserName(String userName);

    void deleteByUserName(String userName);

    List<Order> findByStatusAndExpiresAtBefore(OrderStatus status, LocalDateTime time);



}