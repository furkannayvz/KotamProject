package com.voltdb.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public class PackageDTO {
    @Schema(description = "Package benzersiz ID'si")
    public int PACKAGE_ID;

    @Schema(description = "Package adı")
    public String PACKAGE_NAME;

    @Schema(description = "Package fiyatı")
    public BigDecimal PRICE;

    @Schema(description = "Package dakika miktarı")
    public int AMOUNT_MINUTES;

    @Schema(description = "Package veri miktarı")
    public int AMOUNT_DATA;

    @Schema(description = "Package sms miktarı")
    public int AMOUNT_SMS;

    @Schema(description = "Package süresi")
    public int PERIOD;
}
