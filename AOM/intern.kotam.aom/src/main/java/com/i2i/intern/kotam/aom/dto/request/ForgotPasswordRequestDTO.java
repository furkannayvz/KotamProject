package com.i2i.intern.kotam.aom.dto.request;

// “Şifremi unuttum” işlemi başladığında kullanıcının sistemde kayıtlı olup olmadığını
// kontrol etmek için gerekli bilgileri sunucuya taşımaktır.

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Şifremi unuttum talebi için kullanıcı bilgileri")
public class ForgotPasswordRequestDTO {

    @Schema(description = "T.C. kimlik numarası", example = "12345678901")
    private String nationalId;

    @Schema(description = "Kullanıcının e-posta adresi", example = "zeliha@example.com")
    private String email;

    // Getters and Setters
    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}