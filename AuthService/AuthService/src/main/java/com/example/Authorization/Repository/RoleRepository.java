package com.example.Authorization.Repository;

import com.example.Authorization.Entity.Role;
import com.example.Authorization.Enum.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);

    @Query("SELECT r.name FROM User u JOIN u.roles r WHERE u.id = :userId")
    String findRoleNameStringsByUserId(@Param("userId") Long userId);


    boolean existsById(Long roleId);
}
