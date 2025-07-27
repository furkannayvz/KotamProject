package com.i2i.intern.kotam.aom.dto.request;

// istemciden (frontend/UI) gelen bilgileri sunucuya (backend) taşımak için kullanılır.

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Şifre sıfırlama işlemi için gönderilen bilgiler")
public class ResetPasswordRequestDTO {
    @Schema(description = "Kullanıcının e-posta adresi", example = "zeliha@example.com")
    private String email;

    @Schema(description = "E-posta ile gönderilen doğrulama kodu", example = "864312")
    private String code;

    @Schema(description = "Yeni şifre", example = "YeniSifre123!")
    private String newPassword;

    @Schema(description = "T.C. kimlik numarası", example = "12345678901")
    private String nationalId;

    @Schema(description = "Yeni şifrenin tekrarı", example = "YeniSifre123!")
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

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}