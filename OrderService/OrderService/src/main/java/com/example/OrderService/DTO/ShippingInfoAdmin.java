package com.example.OrderService.DTO;

import java.time.LocalDateTime;

public class ShippingInfoAdmin {
    private String id;
    private String recipientFirstName;
    private String recipientLastName;
    private String phone;
    private String email;
    private String province;
    private String district;
    private String ward;
    private String street;
    private String note;
    private LocalDateTime createdAt;

    // ✅ constructor
    public ShippingInfoAdmin(
            String id,
            String recipientFirstName,
            String recipientLastName,
            String phone,
            String email,
            String province,
            String district,
            String ward,
            String street,
            String note,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.recipientFirstName = recipientFirstName;
        this.recipientLastName = recipientLastName;
        this.phone = phone;
        this.email = email;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.street = street;
        this.note = note;
        this.createdAt = createdAt;
    }

    // ✅ getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRecipientFirstName() { return recipientFirstName; }
    public void setRecipientFirstName(String recipientFirstName) { this.recipientFirstName = recipientFirstName; }

    public String getRecipientLastName() { return recipientLastName; }
    public void setRecipientLastName(String recipientLastName) { this.recipientLastName = recipientLastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
