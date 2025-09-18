package com.example.gateway;

import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class GenSecretKey {

    @Test
    public void generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(256); // 256-bit key
        SecretKey secretKey = keyGen.generateKey();

        // Convert key sang chuỗi Base64 để dễ lưu/cấu hình
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("✅ Your random JWT secret key (base64):");
        System.out.println(base64Key);
    }
}
