package com.example.Authorization.Respone;

import java.time.Instant;

public class RefreshTokenResponse {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RefreshTokenResponse(String token) {
        this.token = token;
    }
}




