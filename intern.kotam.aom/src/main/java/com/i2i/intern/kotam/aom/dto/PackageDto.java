package com.i2i.intern.kotam.aom.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PackageDto {
    private Integer packageId;
    private String packageName;
    private Integer amountMinutes;
    private Integer amountSms;
    private Integer amountData;
    private Double price;
    private Integer period;
    private String status;
    private String description;
}