package com.example.Authorization.Controller;

import com.example.Authorization.Exception.LogoutException;
import com.example.Authorization.GenKey.KeyManagerService;
import com.example.Authorization.DTO.*;
import com.example.Authorization.Respone.PageResponse;
import com.example.Authorization.Respone.TokenResponseDto;
import com.example.Authorization.Respone.UserInfoResponse;
import com.example.Authorization.Service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private AuthService authService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AccountService accountService;

//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;

//    @Autowired
//    private UserSessionService userSessionService;

    @Autowired
    private OAuth2AuthService oAuth2AuthService;

    @Autowired
    private KeyManagerService keyManagerService;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        registerService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginAccount(@RequestBody UserLogin userLogin) {


        TokenResponseDto token = authService.loginUser(userLogin);

        if (token == null || token.getAccessToken() == null || token.getRefreshToken() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponseDto(null, null, null, null));
        }

        return ResponseEntity.ok(token);

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutAccount(@RequestBody RefreshTokenDto refreshTokenDto) {
        try {
            authService.logoutSession(refreshTokenDto);
            return ResponseEntity.noContent().build(); // ✅ 204 No Content
        } catch (LogoutException ex) {
            // business error → 400 hoặc 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            // unexpected error → 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Unexpected error: " + ex.getMessage()));
        }
    }


    @PostMapping("/logout/all")
    public ResponseEntity<?> LogoutAll(@RequestBody RefreshTokenDto refreshTokenDto) {
        try {
            authService.logoutAllSession(refreshTokenDto);
            return ResponseEntity.noContent().build();
        } catch (LogoutException ex) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Unexpected error: " + ex.getMessage()));
        }
    }


    @GetMapping()
    public ResponseEntity<UserInfoResponse> getUserInfo(@RequestParam String userName) {
        UserInfoResponse userInfoResponse = accountService.getUserInfo(userName);
        return ResponseEntity.ok(userInfoResponse);
    }

    @GetMapping("/all")
    public PageResponse<UserResponse> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return accountService.getAllUsers(PageRequest.of(page, size));
    }

    @PatchMapping("/{id}/status")
    public UserResponse changeUserStatus(@PathVariable Long id) {
        return accountService.changeStatus(id);
    }


    @GetMapping("/oauth2/success")
    public void success(@RequestParam("state") String deviceId, OAuth2AuthenticationToken auth, HttpServletResponse response) throws IOException {
        OAuth2User user = auth.getPrincipal();
        String email = user.getAttribute("email");
        String providerId = user.getAttribute("sub");

        TokenResponseDto tokenResponse = oAuth2AuthService.processOAuth2Login(new OAuth2Dto(email, providerId), deviceId);

        String redirectUrl = String.format("http://localhost:4200/oauth-success?accessToken=%s&refreshToken=%s&userName=%s&role=%s", tokenResponse.getAccessToken(), tokenResponse.getRefreshToken(), tokenResponse.getUserName(), tokenResponse.getRole());


        response.sendRedirect(redirectUrl);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestBody RefreshTokenDto refreshToken) {
        TokenResponseDto dto = refreshTokenService.checkValidRefreshTokens(refreshToken);

        if (dto == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/accounts/exist")
    public ResponseEntity<?> validateAccount(@RequestParam String userName) {
        boolean exists = accountService.existByUserName(userName);

        if (exists) {
            return ResponseEntity.ok().build(); // 200 OK, không cần body
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @GetMapping("/publicKey")
    public ResponseEntity<String> getPublicKey() {
        PublicKey publicKey = keyManagerService.getPublicKey();

        if (publicKey == null) {
            return ResponseEntity.status(500).body("❌ Public key not loaded");
        }

        // encode Base64 để client có thể sử dụng verify JWT
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        return ResponseEntity.ok(publicKeyBase64);
    }


    @PutMapping("/updateRole")
    public ResponseEntity<?> updateRole(@RequestParam Long userId, @RequestParam String roleName) {

        accountService.updateUserRole(userId, roleName);
        return ResponseEntity.ok("✅ Role updated successfully");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String username, @Valid @RequestBody ChangePasswordRequest request) {
        accountService.changePassword(username, request);
        return ResponseEntity.ok("✅ Mật khẩu đã được thay đổi thành công");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestParam String username, @Valid @RequestBody ResetPassword resetPassword) {
        accountService.resetPassword(username, resetPassword);
        return ResponseEntity.ok(Map.of("message", "✅ Mật khẩu đã được thay đổi thành công"));
    }


}

