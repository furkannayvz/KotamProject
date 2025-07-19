package com.i2i.intern.kotam.aom.controller;

import com.i2i.intern.kotam.aom.request.LoginCustomerRequest;
import com.i2i.intern.kotam.aom.request.RegisterCustomerRequest;
import com.i2i.intern.kotam.aom.response.AuthenticationResponse;
import com.i2i.intern.kotam.aom.response.LoginResponse;
import com.i2i.intern.kotam.aom.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.voltdb.client.ProcCallException;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller class for Authentication related operations
 */
@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> processCustomerRegistration(@Valid @RequestBody RegisterCustomerRequest request)
            throws SQLException, ClassNotFoundException, IOException, InterruptedException, ProcCallException {

        logger.info("Processing customer registration request");

        AuthenticationResponse response = authService.processCustomerRegistration(request);

        logger.info("Customer registration completed successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateCustomer(@RequestBody LoginCustomerRequest request)
            throws SQLException, ClassNotFoundException {

        logger.info("Processing customer authentication request");

        LoginResponse response = authService.authenticateCustomer(request);

        logger.info("Customer authentication completed successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {

        logger.info("Processing token refresh request");

        authService.handleTokenRefresh(request, response);

        logger.info("Token refresh completed successfully");
    }
}