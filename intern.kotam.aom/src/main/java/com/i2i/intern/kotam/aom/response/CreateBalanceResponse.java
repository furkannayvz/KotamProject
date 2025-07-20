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
public class CreateBalanceResponse {
    @JsonProperty("balance_id")
    private Integer balanceId;

    @JsonProperty("customer_id")
    private Integer customerId;

    @JsonProperty("package_id")
    private Integer packageId;

    @JsonProperty("partition_id")
    private Integer partitionId;
}