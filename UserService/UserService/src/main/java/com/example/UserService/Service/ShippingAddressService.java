package com.example.UserService.Service;

import com.example.UserService.DTO.ShippingAddressRequest;
import com.example.UserService.DTO.ShippingAddressResponse;
import com.example.UserService.Entity.ShippingAddress;
import com.example.UserService.Entity.User;
import com.example.UserService.Mapper.ShippingAddressMapper;
import com.example.UserService.Repository.ShippingAddressRepository;
import com.example.UserService.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service

public class ShippingAddressService {

    private final ShippingAddressRepository shippingRepo;
    private final UserRepository userRepo;

    public ShippingAddressService(ShippingAddressRepository shippingRepo, UserRepository userRepo) {
        this.shippingRepo = shippingRepo;
        this.userRepo = userRepo;
    }

    public List<ShippingAddressResponse> getByUserName(String userName) {
        return shippingRepo.findByUserName(userName)
                .stream()
                .map(ShippingAddressMapper::toResponse)
                .toList();
    }

    public ShippingAddressResponse updateByUserName(String id, String userName, ShippingAddressRequest req) {
        ShippingAddress entity = shippingRepo.findByIdAndUserName(id, userName)
                .orElseThrow(() -> new NoSuchElementException("Not found"));

        ShippingAddress updated = ShippingAddressMapper.toEntity(req);
        entity.setRecipientFirstName(updated.getRecipientFirstName());
        entity.setRecipientLastName(updated.getRecipientLastName());
        entity.setPhoneNumber(updated.getPhoneNumber());
        entity.setProvince(updated.getProvince());
        entity.setWard(updated.getWard());
        entity.setStreet(updated.getStreet());
        entity.setNote(updated.getNote());
        entity.setDefault(updated.isDefault());

        return ShippingAddressMapper.toResponse(shippingRepo.save(entity));
    }

    public void deleteById(String id) {
        if (!shippingRepo.existsById(id)) {
            throw new NoSuchElementException("Không tìm thấy địa chỉ với ID: " + id);
        }

        shippingRepo.deleteById(id);
    }

    public ShippingAddressResponse create(String userName, ShippingAddressRequest req) {
        User user = userRepo.findByUserName(userName)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        ShippingAddress entity = new ShippingAddress();
        entity.setId(UUID.randomUUID().toString());
        entity.setUser(user);
        entity.setUserName(userName);
        entity.setCreatedAt(LocalDateTime.now());

        // Gán các field từ request
        entity.setRecipientFirstName(req.getRecipientFirstName());
        entity.setRecipientLastName(req.getRecipientLastName());
        entity.setPhoneNumber(req.getPhoneNumber());
        entity.setProvince(req.getProvince());
        entity.setWard(req.getWard());
        entity.setStreet(req.getStreet());
        entity.setNote(req.getNote());
        entity.setDefault(req.isDefault());

        ShippingAddress saved = shippingRepo.save(entity);
        return ShippingAddressMapper.toResponse(saved);
    }

}

