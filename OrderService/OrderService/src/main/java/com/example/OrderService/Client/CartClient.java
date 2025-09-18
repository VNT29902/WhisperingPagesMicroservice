package com.example.OrderService.Client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cart-service")
public interface CartClient {

    @DeleteMapping("api/cart/item/{productId}")
    public ResponseEntity<?> removeItem(@PathVariable String productId,
                                        @RequestHeader("X-User-Name") String userName);



}
