package com.example.CartService.FeignClient;

import com.example.CartService.DTO.CartItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@FeignClient(name = "orders-service")
public interface OrdersClient {




    @GetMapping("/api/orders/create")
    ResponseEntity<String> createOrder(@RequestParam String userName,
                                      @RequestBody List<CartItemDTO> items);



}