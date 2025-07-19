package com.i2i.intern.kotam.aom.request;

import lombok.Builder;
import jakarta.validation.constraints.*;

@Builder
public record CreateBalanceRequest(
        @NotNull(message = "Customer ID cannot be null")
        Integer customerId,

        @NotNull(message = "Package ID cannot be null")
        Integer packageId,

        Integer partitionId
) { }