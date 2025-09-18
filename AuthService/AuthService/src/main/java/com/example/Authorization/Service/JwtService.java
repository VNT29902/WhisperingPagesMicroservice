package com.example.Authorization.Service;


import com.example.Authorization.DTO.SessionRedisDto;
import com.example.Authorization.Entity.User;
import com.example.Authorization.GenKey.KeyManagerService;
import com.example.Authorization.Interface.GateWayClient;
import com.example.Authorization.Repository.RoleRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;


@Service
public class JwtService {
    private static final Duration VALIDITY = Duration.ofMinutes(15);



    @Autowired
    private  KeyManagerService keyManagerService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private GateWayClient gateWayClient;




    private PrivateKey getPrivateKey() {
        PrivateKey privateKey = keyManagerService.getPrivateKey();
        if (privateKey == null) {
            throw new IllegalStateException("❌ Private key chưa được load từ PEM");
        }
        return privateKey;
    }
    public String generateAccessToken(User user, String sessionId,String deviceId) {


        String role = roleRepository.findRoleNameStringsByUserId(user.getId());

        // Thêm jti vào JWT
        String jwtToken = Jwts.builder()
                .setSubject(user.getId().toString())
                .setId(sessionId)
                .claim("role",role)
                .claim("device", deviceId)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(900)))
                .signWith(getPrivateKey())
                .compact();

        SessionRedisDto sessionDto = new SessionRedisDto(
                sessionId,
                user.getId(),
                user.getUsername(),
                role,
                deviceId,
                Instant.now().plusSeconds(3600).toEpochMilli()
        );

        gateWayClient.saveSession(sessionDto);

        return jwtToken;
    }


}

