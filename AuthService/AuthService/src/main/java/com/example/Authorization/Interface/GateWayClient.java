package com.example.Authorization.Interface;

import com.example.Authorization.DTO.InfoUserRedis;
import com.example.Authorization.DTO.SessionRedisDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "gateway-service") // nếu dùng service discovery
public interface GateWayClient {

    @PostMapping("/gateway/redis/saveSession")
    ResponseEntity<String> saveSession(@RequestBody SessionRedisDto session);

    @DeleteMapping("/gateway/redis/logoutAll/{userId}")
    ResponseEntity<String> logoutAll(@PathVariable("userId") Long userId);

    // ✅ Logout một session cụ thể
    @DeleteMapping("/gateway/redis/logout/{userId}/{sessionId}")
    ResponseEntity<String> logoutBySession(@PathVariable("userId") Long userId,
                                           @PathVariable("sessionId") String sessionId);

}
