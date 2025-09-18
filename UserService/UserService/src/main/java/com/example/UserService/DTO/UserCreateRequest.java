package com.example.UserService.DTO;


import jakarta.validation.constraints.*;

public class UserCreateRequest {
    @NotBlank @Size(max = 50)
    public String firstName;

    @NotBlank @Size(max = 50)
    public String lastName;

    @Email @NotBlank @Size(max = 120)
    public String email;

    @Size(max = 20)
    public String phone;

    @NotBlank @Size(max = 60)
    public String userName;

    public UserCreateRequest() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
