package com.i2i.intern.kotam.aom.controller;

import com.i2i.intern.kotam.aom.service.BalanceService;
import com.i2i.intern.kotam.aom.request.CreateBalanceRequest;
import com.i2i.intern.kotam.aom.request.BalanceUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.voltdb.client.ProcCallException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * Controller class for Balance related operations
 */
@RestController
@RequestMapping("/v1/api/balance")
public class BalanceController {

    private static final Logger logger = LoggerFactory.getLogger(BalanceController.class);
    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/remainingBalance")
    public ResponseEntity<BalanceService.VoltCustomerBalance> fetchRemainingCustomerBalance(@RequestParam String msisdn)
            throws IOException, InterruptedException, ProcCallException {

        logger.info("Fetching remaining customer balance for MSISDN: {}", msisdn);

        BalanceService.VoltCustomerBalance balance = balanceService.fetchCustomerBalanceByMsisdn(msisdn);

        logger.info("Remaining balance fetched successfully for MSISDN: {}", msisdn);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/oracle/{msisdn}")
    public ResponseEntity<Map<String, Object>> retrieveBalanceFromOracle(@PathVariable String msisdn)
            throws SQLException, ClassNotFoundException {

        logger.info("Retrieving balance from Oracle for MSISDN: {}", msisdn);

        Map<String, Object> balance = balanceService.retrieveBalanceFromOracle(msisdn);

        logger.info("Balance retrieved successfully from Oracle for MSISDN: {}", msisdn);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCustomerBalance(@RequestBody CreateBalanceRequest request)
            throws SQLException, ClassNotFoundException {

        logger.info("Creating balance for customer: {}", request.customerId());

        String response = balanceService.createCustomerBalance(request);

        logger.info("Balance created successfully for customer: {}", request.customerId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCustomerBalance(@RequestBody BalanceUpdateRequest request)
            throws SQLException, ClassNotFoundException {

        logger.info("Updating balance for MSISDN: {}", request.msisdn());

        String response = balanceService.updateCustomerBalance(request);

        logger.info("Balance updated successfully for MSISDN: {}", request.msisdn());
        return ResponseEntity.ok(response);
    }
}
