package com.i2i.intern.kotam.aom.dto.request;

//bir kullanıcının doğrulama (verification) işlemini tamamlaması için kullanılan bir DTO

public class VerificationRequestDTO {
    private String email;
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


