package com.i2i.intern.kotam.aom.controller;

import com.i2i.intern.kotam.aom.dto.CustomerDto;
import com.i2i.intern.kotam.aom.request.RegisterCustomerRequest;
import com.i2i.intern.kotam.aom.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/v1/api/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/getCustomerByMsisdn")
    public ResponseEntity<?> retrieveCustomerByMsisdn(@RequestParam String msisdn) {
        logger.info("Retrieving customer by MSISDN: {}", msisdn);
        try {
            CustomerDto customer = customerService.retrieveCustomerByMsisdn(msisdn);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            logger.error("Error retrieving customer by MSISDN", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve customer.");
        }
    }

    @GetMapping("/getAllCustomers")
    public ResponseEntity<?> fetchAllCustomers() {
        logger.info("Fetching all customers");
        try {
            List<CustomerDto> customers = customerService.fetchAllCustomers();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            logger.error("Error fetching all customers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch customers.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> processCustomerRegistration(@RequestBody RegisterCustomerRequest request) {
        logger.info("Registering customer with MSISDN: {}", request.msisdn());
        try {
            customerService.processCustomerRegistration(request);
            return ResponseEntity.ok("Customer registered successfully");
        } catch (Exception e) {
            logger.error("Customer registration failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed.");
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> updateCustomerPassword(@RequestParam String email,
                                                    @RequestParam String tcNumber,
                                                    @RequestParam String newPassword) {
        logger.info("Updating password for email: {}", email);
        try {
            customerService.updateCustomerPassword(email, tcNumber, newPassword);
            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            logger.error("Password update failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Password update failed.");
        }
    }
}
