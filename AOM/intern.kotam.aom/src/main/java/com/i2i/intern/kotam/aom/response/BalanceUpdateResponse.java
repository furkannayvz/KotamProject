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
public class BalanceUpdateResponse {
    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msisdn")
    private String msisdn;

    @JsonProperty("operation_type")
    private String operationType;

    @JsonProperty("balance_type")
    private String balanceType;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("new_balance")
    private Integer newBalance;
}