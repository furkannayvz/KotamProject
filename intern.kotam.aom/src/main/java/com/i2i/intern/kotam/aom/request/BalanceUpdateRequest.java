package com.i2i.intern.kotam.aom.request;

import lombok.Builder;
import jakarta.validation.constraints.*;

@Builder
public record BalanceUpdateRequest(
        @NotNull(message = "MSISDN cannot be null")
        @NotBlank(message = "MSISDN cannot be blank")
        @Size(min = 10, max = 10, message = "MSISDN must be 10 characters")
        @Pattern(regexp = "^5[0-9]{9}$", message = "MSISDN must start with 5 and be 10 digits")
        String msisdn,

        @NotNull(message = "Operation type cannot be null")
        @NotBlank(message = "Operation type cannot be blank")
        String operationType, // DEBIT, CREDIT

        @NotNull(message = "Balance type cannot be null")
        @NotBlank(message = "Balance type cannot be blank")
        String balanceType, // MAIN_BALANCE, SMS_BALANCE, VOICE_BALANCE, DATA_BALANCE

        @NotNull(message = "Amount cannot be null")
        @Min(value = 1, message = "Amount must be greater than 0")
        Integer amount,

        @NotNull(message = "Description cannot be null")
        @NotBlank(message = "Description cannot be blank")
        String description,

        // Yeni alanlar:
        Integer leftMinutes,
        Integer leftSms,
        Integer leftData
) { }
