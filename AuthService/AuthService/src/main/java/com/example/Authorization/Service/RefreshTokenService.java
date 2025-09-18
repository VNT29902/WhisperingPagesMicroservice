package com.example.Authorization.Service;


import com.example.Authorization.DTO.RefreshTokenDto;
import com.example.Authorization.Entity.RefreshToken;
import com.example.Authorization.Entity.User;
import com.example.Authorization.Interface.GateWayClient;
import com.example.Authorization.Repository.RefreshTokenRepository;
import com.example.Authorization.Repository.RoleRepository;
import com.example.Authorization.Respone.TokenResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private GateWayClient gateWayClient;

    @Autowired
    private  RoleRepository roleRepository;


    public TokenResponseDto checkValidRefreshTokens(RefreshTokenDto refreshToken) {
        Optional<RefreshToken> optionalRt = refreshTokenRepository.findByToken(refreshToken.getRefreshToken());

        if (optionalRt.isEmpty()) {
            return null; // không tìm thấy
        }

        RefreshToken existing = optionalRt.get();

        // ❌ Token hết hạn hoặc đã revoke
        if (existing.isRevoked() || existing.getExpiryDate().isBefore(Instant.now())) {
            return null;
        }

        // ✅ Token hợp lệ → revoke cũ và tạo mới
        User user = existing.getUser();

        existing.setRevoked(true);
        gateWayClient.logoutBySession(user.getId(),optionalRt.get().getSessionId() );
        refreshTokenRepository.save(existing);

        return generateRefreshTokens(user, existing.getDeviceId());
    }



    public TokenResponseDto generateRefreshTokens(User user, String deviceId) {

        String newSessionId = UUID.randomUUID().toString();


        String accessToken = jwtService.generateAccessToken(user, newSessionId,deviceId);

        String role = roleRepository.findRoleNameStringsByUserId(user.getId());


        String newRefreshTokenValue = UUID.randomUUID().toString();


        RefreshToken newRt = new RefreshToken(
                newRefreshTokenValue,
                newSessionId,
                deviceId,
                Instant.now().plusSeconds(60L * 60 * 24 * 7),
                user
        );
        refreshTokenRepository.save(newRt);


        return new TokenResponseDto(accessToken, newRefreshTokenValue, user.getUsername(), role);
    }
}

