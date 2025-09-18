package com.example.gateway.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@Order(1)
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex -> ex

                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers("/api/users/**").permitAll()
                        .pathMatchers("/api/reports/**").permitAll()
                        .pathMatchers("/api/products/**").permitAll()
                        .pathMatchers("/api/cart/**").permitAll()
                        .pathMatchers("/api/orders/**").permitAll()
                        .pathMatchers("/auth/redis/**").permitAll()
                        .pathMatchers("/gateway/redis/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/account/**").permitAll()

                        .anyExchange().authenticated()
                )
                .build();
    }
}

