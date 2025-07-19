package com.i2i.intern.kotam.aom.controller;

import com.i2i.intern.kotam.aom.request.ForgetPasswordRequest;
import com.i2i.intern.kotam.aom.service.ForgetPasswordService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for ForgetPassword related operations
 */
@RestController
@RequestMapping("/v1/api/forgetPassword")
public class ForgetPasswordController {

    private static final Logger logger = LoggerFactory.getLogger(ForgetPasswordController.class);
    private final ForgetPasswordService forgetPasswordService;

    public ForgetPasswordController(ForgetPasswordService forgetPasswordService) {
        this.forgetPasswordService = forgetPasswordService;
    }

    @PostMapping("/reset")
    public ResponseEntity<String> processPasswordReset(@Valid @RequestBody ForgetPasswordRequest request) {

        logger.info("Processing password reset request for email: {}", request.email());

        ResponseEntity<String> response = forgetPasswordService.processPasswordReset(request);

        logger.info("Password reset process completed for email: {}", request.email());
        return response;
    }
}
