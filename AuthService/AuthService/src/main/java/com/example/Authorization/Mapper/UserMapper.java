package com.example.Authorization.Mapper;


import com.example.Authorization.DTO.UserResponse;
import com.example.Authorization.Entity.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.isEnabled(),
                user.getProvider() != null ? user.getProvider().name() : null,
                user.getRoles()
                        .stream()
                        .map(role -> role.getName().name()) // 👈 convert RoleName enum -> String
                        .collect(Collectors.toSet())
        );
    }
}

