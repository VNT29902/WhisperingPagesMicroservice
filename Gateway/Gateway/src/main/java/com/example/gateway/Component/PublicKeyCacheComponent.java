package com.example.gateway.Component;

import com.example.gateway.Client.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class PublicKeyCacheComponent {

    private static final String REDIS_KEY = "gateway:publicKey";

    private final AuthServiceClient authServiceClient;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Autowired
    public PublicKeyCacheComponent(AuthServiceClient authServiceClient,
                                   ReactiveRedisTemplate<String, String> redisTemplate) {
        this.authServiceClient = authServiceClient;
        this.redisTemplate = redisTemplate;
    }

    public Mono<String> fetchAndCachePublicKey() {
        System.out.println("Save public key in redis");
        return authServiceClient.getPublicKey()
                .flatMap(publicKey ->
                        redisTemplate.opsForValue()
                                .set(REDIS_KEY, publicKey)
                                .thenReturn(publicKey)
                );
    }

    public Mono<String> getCachedPublicKey() {
        return redisTemplate.opsForValue().get(REDIS_KEY);
    }

}
