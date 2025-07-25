package com.i2i.intern.kotam.aom.dto.response;

import lombok.Data;

@Data
public class CustomerLoginResponseDTO {
    private String msisdn;
    private String name;
    private String surname;
    private String email;
    private String nationalId;

    // Paket bilgileri
    private String packageName;
    private Double price;
    private Integer amountMinutes;
    private Integer amountData;
    private Integer amountSms;
    private Integer period;
}
