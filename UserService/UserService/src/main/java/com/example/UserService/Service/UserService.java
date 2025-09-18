package com.example.UserService.Service;


import com.example.UserService.DTO.UserCreateRequest;
import com.example.UserService.DTO.UserUpdateRequest;
import com.example.UserService.Entity.User;
import com.example.UserService.Repository.UserRepository;
import com.example.UserService.Response.UserResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public UserResponse create(UserCreateRequest req) {
        if (repo.existsByEmail(req.email)) {
            throw new IllegalStateException("Email đã tồn tại");
        }
        if (repo.existsByUserName(req.userName)) {
            throw new IllegalStateException("Username đã tồn tại");
        }

        User u = new User();
        // Trong service
        u.setId(UUID.randomUUID().toString());
        u.setFirstName(req.firstName);
        u.setLastName(req.lastName);
        u.setEmail(req.email);
        u.setPhone(req.phone);
        u.setUserName(req.userName);
        u.setCreatedAt(LocalDateTime.now());

        return toResponse(repo.save(u));
    }

    @Transactional
    public UserResponse getById(String id) {
        User u = repo.findById(id).orElseThrow(() ->
                new NoSuchElementException("User không tồn tại")
        );
        return toResponse(u);
    }


    @Transactional
    public UserResponse getByUserName(String userName) {
        User u = repo.findByUserName(userName).orElseThrow(() -> new NoSuchElementException("User không tồn tại"));
        return toResponse(u);
    }

    @Transactional
    public UserResponse update(String id, UserUpdateRequest req) {
        User u = repo.findById(id).orElseThrow(() ->
                new NoSuchElementException("User không tồn tại")
        );

        if (!u.getEmail().equalsIgnoreCase(req.email) &&
                repo.existsByEmail(req.email)) {
            throw new IllegalStateException("Email đã tồn tại");
        }

        u.setFirstName(req.firstName);
        u.setLastName(req.lastName);
        u.setEmail(req.email);
        u.setPhone(req.phone);

        return toResponse(u);
    }

    @Transactional
    public void delete(String id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("User không tồn tại");
        }
        repo.deleteById(id);
    }

    // toResponse() giữ nguyên như cũ

    private UserResponse toResponse(User u) {
        UserResponse r = new UserResponse();
        r.id = u.getId();
        r.firstName = u.getFirstName();
        r.lastName = u.getLastName();
        r.email = u.getEmail();
        r.phone = u.getPhone();
        r.userName = u.getUserName();
        r.createdAt = u.getCreatedAt();
        return r;
    }
}


