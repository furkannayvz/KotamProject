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
public class PackageActivationResponse {
    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msisdn")
    private String msisdn;

    @JsonProperty("package_id")
    private Integer packageId;

    @JsonProperty("activation_type")
    private String activationType;

    @JsonProperty("activation_date")
    private String activationDate;
}