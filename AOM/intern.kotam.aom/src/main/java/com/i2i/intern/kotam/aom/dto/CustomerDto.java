package com.i2i.intern.kotam.aom.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class CustomerDto {
    private Integer customerId;
    private String name;
    private String surname;
    private String email;
    private String msisdn;
    private Timestamp sDate;
    private String TCNumber;
}