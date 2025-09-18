package com.example.UserService.Mapper;

import com.example.UserService.DTO.ShippingAddressRequest;
import com.example.UserService.DTO.ShippingAddressResponse;
import com.example.UserService.Entity.ShippingAddress;

public class ShippingAddressMapper {

    public static ShippingAddress toEntity(ShippingAddressRequest req) {
        ShippingAddress entity = new ShippingAddress();
        entity.setRecipientFirstName(req.getRecipientFirstName());
        entity.setRecipientLastName(req.getRecipientLastName());
        entity.setPhoneNumber(req.getPhoneNumber());
        entity.setProvince(req.getProvince());
        entity.setWard(req.getWard());
        entity.setStreet(req.getStreet());
        entity.setNote(req.getNote());
        entity.setDefault(req.isDefault());
        return entity;
    }

    public static ShippingAddressResponse toResponse(ShippingAddress entity) {
        return new ShippingAddressResponse(
                entity.getId(),
                entity.getRecipientFirstName(),
                entity.getRecipientLastName(),
                entity.getPhoneNumber(),
                entity.getProvince(),
                entity.getWard(),
                entity.getStreet(),
                entity.getNote(),
                entity.isDefault()

        );
    }
}
