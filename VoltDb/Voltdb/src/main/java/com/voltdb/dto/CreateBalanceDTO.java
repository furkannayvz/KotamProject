package com.voltdb.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CreateBalanceDTO {

    @Schema(description = "Müşteri MSISDN bilgisi")
    public String MSISDN;

    @Schema(description = "Müşterinin kullandığı Package ID'si")
    public int PACKAGE_ID;

    @Schema(description = "Müşterinin kalan dakika bilgisi")
    public int BAL_LEFT_MINUTES;

    @Schema(description = "Müşterinin kalan sms bilgisi")
    public int BAL_LEFT_SMS;

    @Schema(description = "Müşterinin kalan veri bilgisi")
    public int BAL_LEFT_DATA;
}
