package com.example.UserService.Repository;

import com.example.UserService.Entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, String> {

    List<ShippingAddress> findByUserName(String userName);

    Optional<ShippingAddress> findByIdAndUserName(String id, String userName);

    void deleteByUserName(String userName);

     void deleteById(String id);
}

