package com.example.gateway.DTO;

public class InfoUserRedis {

    private String userName;
    private String role;
    private String version;

    public InfoUserRedis(String userName, String role, String version) {
        this.userName = userName;
        this.role = role;
        this.version = version;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


// getters/setters + constructor
}
