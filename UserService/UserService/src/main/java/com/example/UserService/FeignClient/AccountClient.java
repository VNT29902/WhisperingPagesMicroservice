package com.example.UserService.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service")
public interface AccountClient {
    @GetMapping("/api/auth/accounts/exist")
    ResponseEntity<Void> validateAccount(@RequestParam("userName") String userName);


}