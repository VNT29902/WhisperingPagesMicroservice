package com.example.Authorization.Repository;

import com.example.Authorization.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String Phone);

    @Query(value = "SELECT * FROM users u WHERE u.username = :username", nativeQuery = true)
    Optional<User> findByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM users u WHERE u.email = :input OR u.phone = :input", nativeQuery = true)
    User findByEmailOrPhone(@Param("input") String input);


}
