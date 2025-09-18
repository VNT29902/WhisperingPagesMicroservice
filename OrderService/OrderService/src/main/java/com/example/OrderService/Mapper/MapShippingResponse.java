package com.example.OrderService.Mapper;

import com.example.OrderService.Response.ShippingInfoResponse;
import com.example.OrderService.Entity.ShippingInfo;

public class MapShippingResponse {

    public static ShippingInfoResponse mapperShippingResponse(ShippingInfo shippingInfo) {

        ShippingInfoResponse response = new ShippingInfoResponse();

        response.setId(shippingInfo.getId());

        // Ghép họ tên
        String fullName = shippingInfo.getRecipientLastName() + " " + shippingInfo.getRecipientFirstName();
        response.setFullName(fullName.trim());

        response.setEmail(shippingInfo.getEmail());
        response.setPhone(shippingInfo.getPhone());

        // Gộp địa chỉ từ các phần
        String fullAddress = String.join(", ",
                shippingInfo.getStreet(),
                shippingInfo.getWard(),
                shippingInfo.getProvince()
        );
        response.setAddress(fullAddress);



        return response;
    }
}

