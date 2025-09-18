package com.example.Authorization.GenKey;


import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;

@Service
public class KeyManagerService {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public KeyManagerService() {
        try {
            this.privateKey = PemUtils.readPrivateKey("keys/private.pem"); // relative path từ resources
            System.out.println("✅ Loaded private.pem từ resources");

            this.publicKey = PemUtils.readPublicKey("keys/public.pem"); // relative path từ resources
            System.out.println("✅ Loaded public.pem từ resources");

        } catch (Exception e) {
            throw new RuntimeException("❌ Không load được private.pem/public.pem từ resources", e);
        }
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}





