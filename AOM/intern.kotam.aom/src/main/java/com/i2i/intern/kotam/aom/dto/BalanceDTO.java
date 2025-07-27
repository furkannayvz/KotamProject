package com.i2i.intern.kotam.aom.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

@Schema(description = "Kullanıcının mevcut bakiye durumunu temsil eder")
public class BalanceDTO {

    @Schema(description = "Bakiye ID", example = "101")
    private Long balanceId;

    @Schema(description = "MSISDN (telefon numarası)", example = "5551234567")
    private String msisdn;

    @Schema(description = "Kalan veri (MB)", example = "2500")
    private Long leftData;

    @Schema(description = "Kalan SMS", example = "500")
    private Long leftSms;

    @Schema(description = "Kalan dakika", example = "400")
    private Long leftMinutes;

    @Schema(description = "Paketin alındığı tarih", example = "2024-07-26T10:30:00Z")
    private Timestamp getsDate;

    @Schema(description = "Kullanıcının aktif paketi")
    private PackageDTO packageEntity;

    // Getters and Setters
    public Long getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Long getLeftData() {
        return leftData;
    }

    public void setLeftData(Long leftData) {
        this.leftData = leftData;
    }

    public Long getLeftSms() {
        return leftSms;
    }

    public void setLeftSms(Long leftSms) {
        this.leftSms = leftSms;
    }

    public Long getLeftMinutes() {
        return leftMinutes;
    }

    public void setLeftMinutes(Long leftMinutes) {
        this.leftMinutes = leftMinutes;
    }

    public Timestamp getGetsDate() {
        return getsDate;
    }

    public void setGetsDate(Timestamp getsDate) {
        this.getsDate = getsDate;
    }

    public PackageDTO getPackageEntity() {
        return packageEntity;
    }

    public void setPackageEntity(PackageDTO packageEntity) {
        this.packageEntity = packageEntity;
    }
}
