package com.example.gateway.Component;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component

public class PublicKeyInitializer {

    private final PublicKeyCacheComponent publicKeyCacheComponent;

    @Autowired
    public PublicKeyInitializer(PublicKeyCacheComponent publicKeyCacheComponent) {
        this.publicKeyCacheComponent = publicKeyCacheComponent;
    }

    @PostConstruct
    public void init() {
        System.out.println(">>> Initializing PublicKey Cache...");

        publicKeyCacheComponent.fetchAndCachePublicKey()
                .doOnNext(key -> System.out.println("✅ Public key cached on startup"))
                .doOnError(e -> System.err.println("❌ Final failure to cache public key: " + e.getMessage()))
                .retryWhen(reactor.util.retry.Retry.backoff(5, Duration.ofSeconds(10))  // 👈 Retry up to 5 times, with exponential backoff
                        .doBeforeRetry(retrySignal ->
                                System.out.println("🔁 Retry #" + (retrySignal.totalRetries() + 1) + " after error: " + retrySignal.failure().getMessage()))
                )
                .subscribe();
    }
}
