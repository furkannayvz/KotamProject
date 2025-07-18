package com.i2i.intern.kotam.aom.controller;

import com.i2i.intern.kotam.aom.dto.CustomerDto;
import com.i2i.intern.kotam.aom.service.CustomerService;
import com.i2i.intern.kotam.aom.request.RegisterCustomerRequest;
import com.i2i.intern.kotam.aom.response.AuthenticationResponse;
import com.i2i.intern.kotam.aom.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.voltdb.client.ProcCallException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller class for Customer related operations
 */
@RestController
@RequestMapping("/v1/api/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/getCustomerByMsisdn")
    public ResponseEntity<CustomerDto> retrieveCustomerByMsisdn(@RequestParam String msisdn)
            throws IOException, InterruptedException, ProcCallException {

        logger.info("Retrieving customer by MSISDN: {}", msisdn);

        CustomerDto customer = customerService.retrieveCustomerByMsisdn(msisdn);

        logger.info("Customer retrieved successfully by MSISDN: {}", msisdn);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/getAllCustomers")
    public ResponseEntity<List<CustomerDto>> fetchAllCustomers()
            throws SQLException, ClassNotFoundException {

        logger.info("Fetching all customers");

        List<CustomerDto> customers = customerService.fetchAllCustomers();

        logger.info("All customers fetched successfully, count: {}", customers.size());
        return ResponseEntity.ok(customers);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> processCustomerRegistration(@RequestBody RegisterCustomerRequest request)
            throws SQLException, ClassNotFoundException {

        logger.info("Processing customer registration for MSISDN: {}", request.msisdn());

        AuthenticationResponse response = customerService.processCustomerRegistration(request);

        logger.info("Customer registration completed successfully for MSISDN: {}", request.msisdn());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{msisdn}")
    public ResponseEntity<User> searchUserByMsisdn(@PathVariable String msisdn)
            throws SQLException, ClassNotFoundException {

        logger.info("Searching user by MSISDN: {}", msisdn);

        User user = customerService.searchUserByMsisdn(msisdn);

        logger.info("User found successfully by MSISDN: {}", msisdn);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/password")
    public ResponseEntity<String> updateCustomerPassword(@RequestParam String email,
                                                         @RequestParam String tcNumber,
                                                         @RequestParam String newPassword)
            throws SQLException, ClassNotFoundException {

        logger.info("Updating customer password for email: {}", email);

        customerService.updateCustomerPassword(email, tcNumber, newPassword);

        logger.info("Customer password updated successfully for email: {}", email);
        return ResponseEntity.ok("Password updated successfully");
    }
}
