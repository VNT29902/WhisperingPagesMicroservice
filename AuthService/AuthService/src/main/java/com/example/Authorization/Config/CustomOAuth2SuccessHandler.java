package com.example.Authorization.Config;

import com.example.Authorization.DTO.OAuth2Dto;
import com.example.Authorization.Respone.TokenResponseDto;
import com.example.Authorization.Service.OAuth2AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthService oAuth2AuthService;

    public CustomOAuth2SuccessHandler(OAuth2AuthService oAuth2AuthService) {
        this.oAuth2AuthService = oAuth2AuthService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) authentication;
        OAuth2User user = auth.getPrincipal();

        String email = user.getAttribute("email");
        String providerId = user.getAttribute("sub");
        String deviceId = request.getParameter("state"); // ✅ lấy state từ request

        // 👉 Gọi service gen JWT
        TokenResponseDto tokenResponse =
                oAuth2AuthService.processOAuth2Login(new OAuth2Dto(email, providerId), deviceId);

        // 👉 Redirect về Angular kèm token
        String redirectUrl = String.format(
                "http://localhost:4200/oauth-success?accessToken=%s&refreshToken=%s&userName=%s&role=%s",
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken(),
                tokenResponse.getUserName(),
                tokenResponse.getRole()
        );

        response.sendRedirect(redirectUrl);
    }
}
