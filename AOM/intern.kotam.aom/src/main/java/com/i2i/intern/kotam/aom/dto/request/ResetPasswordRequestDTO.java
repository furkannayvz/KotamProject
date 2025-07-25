package com.i2i.intern.kotam.aom.dto.request;

// istemciden (frontend/UI) gelen bilgileri sunucuya (backend) taşımak için kullanılır.

public class ResetPasswordRequestDTO {
        private String email;
        private String code;
        private String newPassword;
        private String confirmPassword;



    // Getter ve Setter metodları

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}


