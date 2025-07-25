/*
package com.i2i.intern.kotam.aom.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nationalId;
    private String email;
    private String code;
    private LocalDateTime expirationTime;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
}

/*
Kullanıcı “şifremi unuttum” der,

VerificationCode nesnesi oluşturulur,

E-posta ile kullanıcıya doğrulama kodu gönderilir,

Bu nesne veritabanına kayıt edilir,

Kullanıcı kodu girer, sistem bu sınıfa bakarak doğrulama yapar,

Kod geçerliyse işlem devam eder, değilse reddedilir.

*/
