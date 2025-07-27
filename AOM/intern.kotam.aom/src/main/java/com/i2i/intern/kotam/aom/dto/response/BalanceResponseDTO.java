package com.i2i.intern.kotam.aom.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BalanceResponseDTO {
    private String msisdn;
    private int remainingMinutes;
    private int remainingSms;
    private double remainingData;
    private Timestamp sDate;
}
