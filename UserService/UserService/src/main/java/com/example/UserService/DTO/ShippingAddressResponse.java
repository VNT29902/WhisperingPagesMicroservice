package com.example.UserService.DTO;


public class ShippingAddressResponse {

    private String id;
    private String recipientFirstName;
    private String recipientLastName;
    private String phoneNumber;
    private String province;
    private String ward;
    private String street;
    private String note;
    private boolean isDefault;

    public ShippingAddressResponse(String id, String recipientFirstName, String recipientLastName, String phoneNumber, String province, String ward, String street, String note, boolean isDefault) {
        this.id = id;
        this.recipientFirstName = recipientFirstName;
        this.recipientLastName = recipientLastName;
        this.phoneNumber = phoneNumber;
        this.province = province;
        this.ward = ward;
        this.street = street;
        this.note = note;
        this.isDefault = isDefault;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

}
