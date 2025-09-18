package com.example.Authorization.DTO;

import com.example.Authorization.Enum.AuthProvider;

public class OAuth2Dto {

    String email;
    String providerId;



    AuthProvider authProvider;

    public OAuth2Dto(String email, String providerId) {
        this.email = email;

        this.authProvider = AuthProvider.GOOGLE;
        this.providerId = providerId;
    }

    public String getEmail() {
        return email;
    }

    public String getProviderId() {
        return providerId;
    }



    public AuthProvider getAuthProvider() {
        return authProvider;
    }
}
