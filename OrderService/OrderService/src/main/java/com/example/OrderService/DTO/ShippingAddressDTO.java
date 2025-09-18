package com.example.OrderService.DTO;

import jakarta.validation.constraints.NotBlank;

public class ShippingAddressDTO {
    @NotBlank
    private String recipientFirstName;

    @NotBlank
    private String recipientLastName;

    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String province;


    @NotBlank
    private String ward;

    @NotBlank
    private String street;

    private String note;

    public ShippingAddressDTO(String recipientFirstName, String recipientLastName, String email, String phoneNumber, String province, String ward, String street, String note) {
        this.recipientFirstName = recipientFirstName;
        this.recipientLastName = recipientLastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.province = province;
        this.ward = ward;
        this.street = street;
        this.note = note;
    }

    public String getRecipientFirstName() {
        return recipientFirstName;
    }

    public void setRecipientFirstName(String recipientFirstName) {
        this.recipientFirstName = recipientFirstName;
    }

    public String getRecipientLastName() {
        return recipientLastName;
    }

    public void setRecipientLastName(String recipientLastName) {
        this.recipientLastName = recipientLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


}
