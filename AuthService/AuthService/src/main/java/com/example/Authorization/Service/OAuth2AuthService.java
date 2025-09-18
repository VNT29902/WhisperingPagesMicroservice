package com.example.Authorization.Service;


import com.example.Authorization.DTO.OAuth2Dto;
import com.example.Authorization.Entity.Role;
import com.example.Authorization.Entity.User;
import com.example.Authorization.Enum.RoleName;
import com.example.Authorization.Repository.RoleRepository;
import com.example.Authorization.Repository.UserRepository;
import com.example.Authorization.Respone.TokenResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

import static com.example.Authorization.Enum.RoleName.ROLE_USER;

@Service
public class OAuth2AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;


    public TokenResponseDto processOAuth2Login(OAuth2Dto oAuth2Dto,String deviceId) {
        Role defaultRole = roleRepository.findByName(ROLE_USER)
                .orElseThrow(() -> new RuntimeException("❌ Default role not found"));

        String email = oAuth2Dto.getEmail();
        String username = email.split("@")[0];

        // Tìm user theo username
        Optional<User> optionalUser = userRepository.findByUsername(username);

        // Nếu chưa tồn tại thì tạo mới
        if (optionalUser.isEmpty()) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setProvider(oAuth2Dto.getAuthProvider());
            newUser.setProviderId(oAuth2Dto.getProviderId());
            newUser.setEnabled(true);
            newUser.setRoles(Set.of(defaultRole));
            userRepository.save(newUser);

            // Gọi login với user mới
            return refreshTokenService.generateRefreshTokens(newUser,deviceId);
        }

        // Gọi login với user đã có
        return refreshTokenService.generateRefreshTokens(optionalUser.get(),deviceId);
    }

}
