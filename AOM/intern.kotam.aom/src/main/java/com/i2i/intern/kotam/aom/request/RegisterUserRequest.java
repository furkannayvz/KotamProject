package com.i2i.intern.kotam.aom.request;

import lombok.Builder;
import jakarta.validation.constraints.*;

@Builder
public record RegisterUserRequest(
        @NotNull(message = "MSISDN cannot be null")
        @NotBlank(message = "MSISDN cannot be blank")
        @Size(min = 10, max = 10, message = "MSISDN must be 10 characters")
        @Pattern(regexp = "^5[0-9]{9}$", message = "MSISDN must start with 5 and be 10 digits")
        String msisdn,

        @NotNull(message = "Name cannot be null")
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,

        @NotNull(message = "Surname cannot be null")
        @NotBlank(message = "Surname cannot be blank")
        @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
        String surname,

        @NotNull(message = "Email cannot be null")
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be valid format")
        String email,

        @NotNull(message = "Password cannot be null")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String password,

        @NotNull(message = "TC Number cannot be null")
        @NotBlank(message = "TC Number cannot be blank")
        @Size(min = 11, max = 11, message = "TC Number must be 11 characters")
        @Pattern(regexp = "\\d{11}", message = "TC Number must contain only digits")
        String tcNumber,

        @NotNull(message = "Role cannot be null")
        @NotBlank(message = "Role cannot be blank")
        String role
) { }