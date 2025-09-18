package com.example.gateway.Redis;



import com.example.gateway.DTO.InfoUserRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


//    public void savePublicKey(String key, String value, Duration ttl) {
//        redisTemplate.opsForValue().set(key, value);
//        if (ttl != null) redisTemplate.expire(key, ttl);
//    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

//    public void save(String key, String value) {
//        redisTemplate.opsForValue().set(key, value);
//    }
//
//    public void delete(String key) {
//        redisTemplate.delete(key);
//    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // ========= PER-USER (cũ) =========
    public void saveUserSession(InfoUserRedis sessionData, Duration ttl) {
        String key = sessionData.getUserName(); // userName làm key

        Map<String, String> hash = new HashMap<>();
        hash.put("role", sessionData.getRole());
        hash.put("version", sessionData.getVersion());

        redisTemplate.opsForHash().putAll(key, hash);
        redisTemplate.expire(key, ttl);
    }

//    public String getRoleByUserName(String userName) {
//        Object role = redisTemplate.opsForHash().get(userName, "role");
//        return role != null ? role.toString() : null;
//    }

//    public String getVersionByUserName(String userName) {
//        Object version = redisTemplate.opsForHash().get(userName, "version");
//        return version != null ? version.toString() : null;
//    }

    public void updateVersionByUserName(String userName, String newVersion) {
        if (!exists(userName)) {
            throw new RuntimeException("❌ User not found in Redis: " + userName);
        }
        redisTemplate.opsForHash().put(userName, "version", newVersion);
        redisTemplate.expire(userName, Duration.ofDays(3));
    }

    public InfoUserRedis getUserSession(String userName) {
        String role = (String) redisTemplate.opsForHash().get(userName, "role");
        String version = (String) redisTemplate.opsForHash().get(userName, "version");

        if (role == null && version == null) return null;
        return new InfoUserRedis(userName, role, version);
    }

    // ========= PER-SESSION (mới) =========
    public void saveSession(String sessionId, Map<String, String> values, Duration ttl) {
        String key = "session:" + sessionId;
        redisTemplate.opsForHash().putAll(key, values);
        if (ttl != null) redisTemplate.expire(key, ttl);
    }

    public Map<String, String> getSession(String sessionKey) {
        Map<Object, Object> raw = redisTemplate.opsForHash().entries(sessionKey);
        if (raw == null || raw.isEmpty()) return null;

        Map<String, String> result = new HashMap<>();
        raw.forEach((k, v) -> result.put((String) k, (String) v));
        return result;
    }

//    public void deleteSession(String sessionId) {
//        redisTemplate.delete("session:" + sessionId);
//    }
//
//    public void addSessionToUser(Long userId, String sessionId) {
//        redisTemplate.opsForSet().add("user:" + userId + ":sessions", sessionId);
//    }

    public Set<String> getUserSessions(Long userId) {
        return redisTemplate.opsForSet().members("user:" + userId + ":sessions");
    }

//    public void deleteUserSessions(Long userId) {
//        Set<String> sessions = getUserSessions(userId);
//        if (sessions != null) {
//            sessions.forEach(s -> redisTemplate.delete("session:" + s));
//        }
//        redisTemplate.delete("user:" + userId + ":sessions");
//    }
}