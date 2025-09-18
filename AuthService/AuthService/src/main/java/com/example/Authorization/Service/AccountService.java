package com.example.Authorization.Service;

import com.example.Authorization.DTO.ChangePasswordRequest;
import com.example.Authorization.DTO.ResetPassword;
import com.example.Authorization.DTO.UserResponse;
import com.example.Authorization.Enum.RoleName;
import com.example.Authorization.Entity.Role;
import com.example.Authorization.Entity.User;
import com.example.Authorization.Mapper.UserMapper;
import com.example.Authorization.Repository.RoleRepository;
import com.example.Authorization.Repository.UserRepository;
import com.example.Authorization.Respone.PageResponse;
import com.example.Authorization.Respone.UserInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    private final PasswordEncoder passwordEncoder;

    public AccountService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existByUserName(String userName) {
        return userRepository.existsByUsername(userName);
    }


    public UserInfoResponse getUserInfo(String userName) {
        Optional<User> optionalUser = userRepository.findByUsername(userName);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("❌ Invalid userName");
        }

        User user = optionalUser.get();

        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setEmail(user.getEmail());
        userInfoResponse.setFirstName(user.getFirstName());
        userInfoResponse.setLastName(user.getLastName());
        userInfoResponse.setPhone(user.getPhone());

        return userInfoResponse;
    }

    @Transactional
    public void updateUserRole(Long userId, String roleNameStr) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("❌ User not found"));

        RoleName roleName;
        try {
            roleName = RoleName.valueOf(roleNameStr);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("❌ Invalid role name");
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("❌ Role not found"));

        // Xóa hết roles cũ (nếu chỉ muốn 1 role duy nhất)
        user.getRoles().clear();

        // Thêm role mới
        user.getRoles().add(role);

        userRepository.save(user);
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        // Kiểm tra xác nhận mật khẩu
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("❌ Mật khẩu xác nhận không khớp");
        }

        // Tìm user (không dùng orElseThrow)
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("❌ Không tìm thấy người dùng");
        }

        User user = optionalUser.get();

        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("❌ Mật khẩu hiện tại không đúng");
        }

        // Mã hóa và cập nhật mật khẩu mới
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }


    public void resetPassword(String username, ResetPassword resetPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("❌ Không tìm thấy người dùng"));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(r -> r.getName().name().equals("ROLE_ADMIN"));
        if (isAdmin) {
            throw new IllegalStateException("❌ Không thể reset mật khẩu cho tài khoản ADMIN");
        }

        if (resetPassword.getResetPass() == null || resetPassword.getResetPass().isBlank()) {
            throw new IllegalArgumentException("❌ Mật khẩu mới không được để trống");
        }

        // encode password trước khi lưu
        String encoded = passwordEncoder.encode(resetPassword.getResetPass());
        user.setPassword(encoded);

        userRepository.save(user);
    }

    public UserResponse changeStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        user.setEnabled(!user.isEnabled());
        User updated = userRepository.save(user);

        return UserMapper.toResponse(updated);
    }


    public PageResponse<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);

        List<UserResponse> responses = page.getContent().stream()
                .map(UserMapper::toResponse)
                .toList();

        return new PageResponse<>(
                responses,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }





}





