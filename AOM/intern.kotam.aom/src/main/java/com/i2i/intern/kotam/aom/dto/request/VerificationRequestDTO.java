package com.i2i.intern.kotam.aom.dto.request;

//bir kullanıcının doğrulama (verification) işlemini tamamlaması için kullanılan bir DTO

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Kullanıcının doğrulama kodunu kontrol etmek için gönderdiği bilgiler")
public class VerificationRequestDTO {

    @Schema(description = "Kullanıcının e-posta adresi", example = "zeliha@example.com")
    private String email;

    @Schema(description = "Doğrulama kodu", example = "86431")
    private String code;

    // Getters and Setters
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
}


