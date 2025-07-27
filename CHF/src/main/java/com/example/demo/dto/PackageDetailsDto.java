package com.example.demo.dto;

public class PackageDetailsDto {
    private int amountMinutes;
    private int amountData;
    private int amountSms;
    private String packageName;

    public PackageDetailsDto(int amountMinutes, int amountData, int amountSms, String packageName) {
        this.amountMinutes = amountMinutes;
        this.amountData = amountData;
        this.amountSms = amountSms;
        this.packageName = packageName;
    }

    public int getAmountMinutes() {
        return amountMinutes;
    }

    public int getAmountData() {
        return amountData;
    }

    public int getAmountSms() {
        return amountSms;
    }

    public String getPackageName() {
        return packageName;
    }
}
