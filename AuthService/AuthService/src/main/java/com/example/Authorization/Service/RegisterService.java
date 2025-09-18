package com.example.Authorization.Service;

import com.example.Authorization.DTO.RegisterRequest;

import com.example.Authorization.Entity.Role;
import com.example.Authorization.Entity.User;
import com.example.Authorization.Enum.AuthProvider;
import com.example.Authorization.Repository.RoleRepository;
import com.example.Authorization.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

import static com.example.Authorization.Enum.RoleName.ROLE_USER;

@Service
public class RegisterService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired

    private   RoleRepository roleRepository;
    @Autowired
    private   PasswordEncoder passwordEncoder;


    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "📧 Email is already in use");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "📱 Phone is already in use");
        }

        Role defaultRole = roleRepository.findByName(ROLE_USER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Default role not found"));

        User user = new User();
        user.setUsername(request.getEmail().split("@")[0]);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setProvider(AuthProvider.LOCAL);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setRoles(Set.of(defaultRole));

        userRepository.save(user);
    }
}

