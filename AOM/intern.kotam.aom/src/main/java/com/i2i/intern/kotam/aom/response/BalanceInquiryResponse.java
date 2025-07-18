package com.i2i.intern.kotam.aom.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceInquiryResponse {
    @JsonProperty("msisdn")
    private String msisdn;

    @JsonProperty("balance_level_minutes")
    private Integer balanceLevelMinutes;

    @JsonProperty("balance_level_sms")
    private Integer balanceLevelSMS;

    @JsonProperty("balance_level_data")
    private Integer balanceLevelData;

    @JsonProperty("balance_level_money")
    private Integer balanceLevelMoney;
}