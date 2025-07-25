package com.i2i.intern.kotam.aom.dto.request;

// “Şifremi unuttum” işlemi başladığında kullanıcının sistemde kayıtlı olup olmadığını
// kontrol etmek için gerekli bilgileri sunucuya taşımaktır.

public class ForgotPasswordRequestDTO {
    private String nationalId;
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
