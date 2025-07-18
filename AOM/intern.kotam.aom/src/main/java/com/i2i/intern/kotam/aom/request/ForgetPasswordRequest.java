package com.i2i.intern.kotam.aom.request;

import lombok.Builder;
import jakarta.validation.constraints.*;

@Builder
public record ForgetPasswordRequest(
        @NotNull(message = "Email cannot be null")
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be valid format")
        String email,

        @NotNull(message = "TC Number cannot be null")
        @NotBlank(message = "TC Number cannot be blank")
        @Size(min = 11, max = 11, message = "TC Number must be 11 characters")
        @Pattern(regexp = "\\d{11}", message = "TC Number must contain only digits")
        String TCNumber
) { }