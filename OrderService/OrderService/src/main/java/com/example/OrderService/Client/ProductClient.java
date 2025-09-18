package com.example.OrderService.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
public interface ProductClient {
    @PostMapping("/api/products/{id}/confirm")
    ResponseEntity<String> confirmStock(@PathVariable String id,
                                        @RequestParam int quantity);

    /** Giữ hàng tạm thời cho thanh toán online (TTL tính bằng giây) */
    @PostMapping("/api/products/{id}/reserve")
    ResponseEntity<String> reserveStock(@PathVariable String id,
                                        @RequestParam int quantity,
                                        @RequestParam int ttlSeconds);

    /** Trả lại hàng đã reserve */
    @PutMapping("/api/products/{id}/release")
    void releaseStock(@PathVariable String id,
                      @RequestParam int quantity);
}
