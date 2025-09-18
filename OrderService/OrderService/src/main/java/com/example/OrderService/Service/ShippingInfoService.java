package com.example.OrderService.Service;

import com.example.OrderService.DTO.ShippingAddressDTO;
import com.example.OrderService.DTO.ShippingInfoAdmin;
import com.example.OrderService.Response.ShippingInfoResponse;
import com.example.OrderService.Entity.Order;
import com.example.OrderService.Entity.ShippingInfo;
import com.example.OrderService.Mapper.MapShippingResponse;
import com.example.OrderService.Repository.OrderRepository;
import com.example.OrderService.Repository.ShippingInfoRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ShippingInfoService {

    @Autowired
    private  ShippingInfoRepository shippingInfoRepository;

    @Autowired
    private  OrderRepository orderRepository;



    public ShippingInfoResponse addShippingInfo(String orderId, ShippingAddressDTO request) {

        // 1. Validate order existence
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("❌ Order with ID [" + orderId + "] not found"));

        // 2. Prevent duplicate shipping info
        if (shippingInfoRepository.existsByOrderId(orderId)) {
            throw new IllegalStateException("❌ Shipping info already exists for orderId [" + orderId + "]");
        }

        // 3. Generate unique shipping ID
        String id = "SHP-" + RandomStringUtils.randomAlphanumeric(8).toUpperCase();


        ShippingInfo info = new ShippingInfo();
        info.setId(id);
        info.setRecipientFirstName(request.getRecipientFirstName());
        info.setRecipientLastName(request.getRecipientLastName());
        info.setPhone(request.getPhoneNumber());
        info.setEmail(request.getEmail());
        info.setProvince(request.getProvince());
        info.setWard(request.getWard());
        info.setStreet(request.getStreet());
        info.setNote(request.getNote());
        info.setOrder(order);

        // 5. Save and return mapped response
        ShippingInfo saved = shippingInfoRepository.save(info);

        return MapShippingResponse.mapperShippingResponse(saved);
    }

    public ShippingInfo getByOrderId(String orderId) {
        return shippingInfoRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoSuchElementException("❌ Shipping info not found for orderId: " + orderId));
    }


    public ShippingInfoResponse getShippingInfoByOrderId(String orderId) {
        ShippingInfo entity = shippingInfoRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new RuntimeException("Shipping info not found for order " + orderId));

        Order order = entity.getOrder(); // lấy order để có status + paymentMethod

        String fullName = entity.getRecipientFirstName() + " " + entity.getRecipientLastName();
        String address = entity.getStreet() + ", " + entity.getWard() + ", "
                + ", " + entity.getProvince();

        return new ShippingInfoResponse(
                entity.getId(),
                fullName,
                entity.getEmail(),
                entity.getPhone(),
                address,
                order.getStatus().name(),
                order.getPaymentMethod().name()
        );
    }
}

