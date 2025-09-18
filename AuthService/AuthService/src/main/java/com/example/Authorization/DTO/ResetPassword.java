package com.example.Authorization.DTO;

public class ResetPassword {


    private String resetPass;


    public ResetPassword(String resetPass) {
        this.resetPass = resetPass;
    }

    public String getResetPass() {
        return resetPass;
    }

    public void setResetPass(String resetPass) {
        this.resetPass = resetPass;
    }
}
