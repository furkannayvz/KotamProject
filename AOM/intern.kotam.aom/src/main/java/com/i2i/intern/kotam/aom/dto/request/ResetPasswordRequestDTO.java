package com.i2i.intern.kotam.aom.dto.request;


public class ResetPasswordRequestDTO {

    private String email;
    private String nationalId;
    private String newPassword;

    // Getter ve Setter metodları

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
