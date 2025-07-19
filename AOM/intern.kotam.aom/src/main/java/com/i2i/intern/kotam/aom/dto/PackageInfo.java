package com.i2i.intern.kotam.aom.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PackageInfo {
    private String packageName;
    private int amountMinutes;
    private int amountSms;
    private int amountData;
    private double price;
    private int period;
}