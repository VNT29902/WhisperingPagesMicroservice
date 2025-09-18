package com.example.CartService.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "product-service")
public interface ProductClient {
    @GetMapping("/api/products/{id}/price")
    BigDecimal getPriceProduct(@PathVariable String id);



    @PostMapping("/api/products/{id}/order")
    ResponseEntity<String> placeOrder(@PathVariable String id, @RequestParam int quantity);

    @GetMapping("/api/products/{id}/checkStock")
    public ResponseEntity<String> checkStock(@PathVariable String id, @RequestParam int quantity);


}