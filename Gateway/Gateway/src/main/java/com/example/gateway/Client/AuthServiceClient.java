package com.example.gateway.Client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class AuthServiceClient {

    private final WebClient.Builder webClientBuilder;

    public AuthServiceClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<String> getPublicKey( ) {
        return webClientBuilder.build()
                .get()
                .uri("http://auth-service/api/auth/publicKey")   // <-- Gọi theo service name
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .retry(3);
    }

}
