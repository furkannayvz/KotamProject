package com.i2i.intern.kotam.aom.controller;

import com.i2i.intern.kotam.aom.dto.request.*;
import com.i2i.intern.kotam.aom.service.EmailService;
import com.i2i.intern.kotam.aom.service.PasswordResetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Şifre Kurtarma", description = "Şifre sıfırlama, kod gönderme ve doğrulama işlemleri")
public class ForgotPasswordController {

    private final EmailService emailService;
    private final PasswordResetService passwordResetService;

    public ForgotPasswordController(EmailService emailService, PasswordResetService passwordResetService) {
        this.emailService = emailService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        emailService.sendVerificationCode(request.getEmail(), request.getNationalId());
        return ResponseEntity.ok("Verification code sent to email.");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody VerificationRequestDTO request) {
        boolean valid = emailService.verifyCode(request.getEmail(), request.getCode());
        if (valid) return ResponseEntity.ok("Code verified.");
        return ResponseEntity.badRequest().body("Invalid or expired code.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match.");
        }

        boolean success = passwordResetService.resetPassword(
                request.getEmail(),
                request.getNationalId(),
                request.getCode(),
                request.getNewPassword()
        );

        if (success) return ResponseEntity.ok("Password has been reset successfully.");
        return ResponseEntity.badRequest().body("Invalid code or email.");
    }
}

