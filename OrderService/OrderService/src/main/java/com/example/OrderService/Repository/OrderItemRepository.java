package com.example.OrderService.Repository;

import com.example.OrderService.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {


    List<OrderItem> findByOrder_Id(String orderId);



}