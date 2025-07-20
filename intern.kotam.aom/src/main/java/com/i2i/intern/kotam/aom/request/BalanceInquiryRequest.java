package com.i2i.intern.kotam.aom.request;

import lombok.Builder;
import jakarta.validation.constraints.*;

@Builder
public record BalanceInquiryRequest(
        @NotNull(message = "MSISDN cannot be null")
        @NotBlank(message = "MSISDN cannot be blank")
        @Size(min = 10, max = 10, message = "MSISDN must be 10 characters")
        @Pattern(regexp = "^5[0-9]{9}$", message = "MSISDN must start with 5 and be 10 digits")
        String msisdn,

        String balanceType // MAIN, SMS, VOICE, DATA, ALL
) { }