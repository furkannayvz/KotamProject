package com.i2i.intern.kotam.aom.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class LoginRequestDTO {

    @Schema(description = "Müşterinin telefon numarası", example = "5551234567")
    private String msisdn;

    @Schema(description = "Müşterinin şifresi", example = "1234abcDEF!")
    private String password;

    // Getters and Setters
    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
