package com.example.gateway.Controller;

import com.example.gateway.DTO.SessionRedisDto;
import com.example.gateway.Redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/gateway/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

   @Autowired
    private  StringRedisTemplate redisTemplate;


    @PostMapping("/saveSession")
    public ResponseEntity<String> saveSession(@RequestBody SessionRedisDto session) {
        try {
            String key = "session:" + session.getSessionId();
            Map<String, String> values = new HashMap<>();
            values.put("userId", session.getUserId().toString());
            values.put("username", session.getUsername());
            values.put("role", session.getRole());
            values.put("deviceId", session.getDeviceId());

            redisTemplate.opsForHash().putAll(key, values);
            redisTemplate.expire(key, Duration.ofMinutes(30));


            redisTemplate.opsForSet().add("user:" + session.getUserId() + ":sessions", session.getSessionId());

            return ResponseEntity.ok("Session saved: " + session.getSessionId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/logoutAll/{userId}")
    public ResponseEntity<String> logoutAll(@PathVariable Long userId) {
        try {
            String userSessionsKey = "user:" + userId + ":sessions";

            // Lấy tất cả sessionId của user
            Set<String> sessions = redisTemplate.opsForSet().members(userSessionsKey);

            if (sessions != null) {
                for (String sessionId : sessions) {
                    redisTemplate.delete("session:" + sessionId);
                }
            }

            // Xoá luôn danh sách session của user
            redisTemplate.delete(userSessionsKey);

            return ResponseEntity.ok("✅ All sessions logged out for user " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error: " + e.getMessage());
        }
    }



    @DeleteMapping("/logout/{userId}/{sessionId}")
    public ResponseEntity<String> logoutBySession(@PathVariable Long userId, @PathVariable String sessionId) {
        try {
            // Xoá hash session
            redisTemplate.delete("session:" + sessionId);

            // Xoá sessionId khỏi set của user
            redisTemplate.opsForSet().remove("user:" + userId + ":sessions", sessionId);

            return ResponseEntity.ok("✅ Session " + sessionId + " logged out for user " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error: " + e.getMessage());
        }
    }


}
