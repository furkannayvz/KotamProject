package com.i2i.intern.kotam.aom.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API'de kullanılan paket bilgilerinin DTO modeli")
public class PackageDTO {

    @Schema(description = "Paket ID", example = "2")
    private Long id;

    @Schema(description = "Veri kotası (MB)", example = "5000")
    private int dataQuota;

    @Schema(description = "SMS kotası", example = "1000")
    private int smsQuota;

    @Schema(description = "Dakika kotası", example = "500")
    private int minutesQuota;

    @Schema(description = "Paket fiyatı (₺)", example = "39.90")
    private double price;

    @Schema(description = "Paket süresi (gün)", example = "30")
    private int period;

    @Schema(description = "Paket adı", example = "Sosyal Paket")
    private String packageName;
    
   // Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDataQuota() {
        return dataQuota;
    }

    public void setDataQuota(int dataQuota) {
        this.dataQuota = dataQuota;
    }

    public int getSmsQuota() {
        return smsQuota;
    }

    public void setSmsQuota(int smsQuota) {
        this.smsQuota = smsQuota;
    }

    public int getMinutesQuota() {
        return minutesQuota;
    }

    public void setMinutesQuota(int minutesQuota) {
        this.minutesQuota = minutesQuota;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}