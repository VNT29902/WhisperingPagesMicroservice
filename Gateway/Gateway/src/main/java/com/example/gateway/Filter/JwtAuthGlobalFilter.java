package com.example.gateway.Filter;

import com.example.gateway.Redis.RedisService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;


@Component
public class JwtAuthGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisService redisService;



    private PublicKey publicKey;

    private void loadPublicKeyIfNecessary() {
        if (this.publicKey != null) return;
        try {
            String base64Key = redisService.get("gateway:publicKey");
            if (base64Key != null && !base64Key.isEmpty()) {
                byte[] decoded = Base64.getDecoder().decode(base64Key);
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                this.publicKey = kf.generatePublic(keySpec);
                System.out.println("✅ Public key loaded dynamically from Redis");
            } else {
                System.err.println("⚠️ Public key not available in Redis");
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to load public key in JwtAuthGlobalFilter");
            e.printStackTrace();
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        HttpMethod method = exchange.getRequest().getMethod();

        // Bypass public routes
        AntPathMatcher pathMatcher = new AntPathMatcher();
        if (pathMatcher.match("/api/auth/**", path)
                || pathMatcher.match("/actuator/**", path)
                || pathMatcher.match("/api/products/**", path)) {
            return chain.filter(exchange);
        }

        // Lấy token từ header
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        loadPublicKeyIfNecessary();

        try {
            // Parse JWT
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token);

            Claims payload = claims.getPayload();
            String userId = payload.getSubject();
            String jti = payload.getId(); // sessionId
            String roleInToken = payload.get("role", String.class);

            // ✅ Kiểm tra session trong Redis
            Map<String, String> sessionData = redisService.getSession("session:" + jti);
            if (sessionData == null || sessionData.isEmpty()) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // So khớp userId và role để chắc chắn
            if (!userId.equals(sessionData.get("userId"))
                    || !roleInToken.equals(sessionData.get("role"))) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            // ✅ Cho qua + gắn thông tin vào header
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", userId)
                    .header("X-Role", sessionData.get("role"))
                    .header("X-Device-Id", sessionData.get("deviceId"))
                    .header("X-User-Name", sessionData.get("username"))

                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (JwtException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
