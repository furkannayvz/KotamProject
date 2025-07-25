package com.i2i.intern.kotam.aom.dto.request;

// kullanıcının mevcut şifresini değiştirirken kullanılir

public class ChangePasswordRequestDTO {
    private String msisdn;
    private String currentPassword;
    private String newPassword;

    // Getters and Setters
    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
