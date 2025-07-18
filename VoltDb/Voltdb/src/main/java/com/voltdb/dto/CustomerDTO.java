package com.voltdb.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

public class CustomerDTO {
    @Schema(description = "Müşteri MSISDN")
    public String msisdn;

    @Schema(description = "Müşteri adı")
    public String name;

    @Schema(description = "Müşteri soyadı")
    public String surname;

    @Schema(description = "Müşteri emaili")
    public String email;

    @Schema(description = "Müşteri şifresi")
    public String password;

    @Schema(description = "Müşteri kayıt tarihi")
    public Timestamp sdate;

    @Schema(description = "Müşteri kimlik numarası")
    public String national_id;

}

