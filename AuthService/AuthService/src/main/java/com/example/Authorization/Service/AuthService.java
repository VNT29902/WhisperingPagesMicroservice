package com.example.Authorization.Service;

import com.example.Authorization.DTO.RefreshTokenDto;
import com.example.Authorization.DTO.UserLogin;
import com.example.Authorization.Entity.RefreshToken;
import com.example.Authorization.Entity.User;
import com.example.Authorization.Exception.LogoutException;
import com.example.Authorization.GenKey.KeyManagerService;
import com.example.Authorization.Interface.GateWayClient;
import com.example.Authorization.Repository.RefreshTokenRepository;
import com.example.Authorization.Repository.RoleRepository;
import com.example.Authorization.Repository.UserRepository;
import com.example.Authorization.Respone.TokenResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service

public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KeyManagerService keyManagerService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private GateWayClient gateWayClient;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    private static final Duration VALIDITY = Duration.ofMinutes(30);


    public TokenResponseDto loginUser(UserLogin userLogin) {
        String input = userLogin.getEmailOrPhone();
        String password = userLogin.getPassword();

        User user = userRepository.findByEmailOrPhone(input);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return new TokenResponseDto( null, null,null,null);
        }

        return refreshTokenService.generateRefreshTokens(user, userLogin.getDeviceId());
    }


    public void logoutSession(RefreshTokenDto refreshTokenDto) {
        RefreshToken existing = refreshTokenRepository.findByToken(refreshTokenDto.getRefreshToken())
                .orElseThrow(() -> new LogoutException("Refresh token not found"));

        if (existing.isRevoked()) {
            throw new LogoutException("Refresh token already revoked");
        }

        User user = existing.getUser();

        try {
            // revoke token
            existing.setRevoked(true);

            // gọi Gateway xoá session
            gateWayClient.logoutBySession(user.getId(), existing.getSessionId());

            refreshTokenRepository.save(existing);

        } catch (Exception e) {
            throw new LogoutException("Failed to logout session: " + e.getMessage());
        }
    }



    public void logoutAllSession(RefreshTokenDto refreshTokenDto) {
        RefreshToken existing = refreshTokenRepository.findByToken(refreshTokenDto.getRefreshToken())
                .orElseThrow(() -> new LogoutException("Refresh token not found"));

        User user = existing.getUser();

        try {
            // ✅ Revoke tất cả refresh token của user
            List<RefreshToken> tokens = refreshTokenRepository.findAllByUserId(user.getId());
            for (RefreshToken token : tokens) {
                token.setRevoked(true);
            }
            refreshTokenRepository.saveAll(tokens);

            // ✅ Gọi Gateway xoá toàn bộ session
            gateWayClient.logoutAll(user.getId());

        } catch (Exception e) {
            throw new LogoutException("Failed to logout all sessions: " + e.getMessage());
        }
    }

}

