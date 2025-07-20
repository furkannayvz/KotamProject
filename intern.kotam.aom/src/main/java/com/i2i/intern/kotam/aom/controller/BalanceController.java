package com.i2i.intern.kotam.aom.controller;

import com.i2i.intern.kotam.aom.request.BalanceUpdateRequest;
import com.i2i.intern.kotam.aom.request.CreateBalanceRequest;
import com.i2i.intern.kotam.aom.service.BalanceService;
import com.i2i.intern.kotam.aom.service.BalanceService.VoltCustomerBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.voltdb.client.ProcCallException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/balance")
public class BalanceController {

    private static final Logger logger = LoggerFactory.getLogger(BalanceController.class);
    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/remainingBalance")
    public ResponseEntity<VoltCustomerBalance> fetchRemainingCustomerBalance(@RequestParam String msisdn)
            throws IOException, InterruptedException, ProcCallException {
        logger.info("Fetching remaining customer balance for MSISDN: {}", msisdn);
        VoltCustomerBalance balance = balanceService.fetchCustomerBalanceByMsisdn(msisdn);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/oracle/{msisdn}")
    public ResponseEntity<Map<String, Object>> retrieveBalanceFromOracle(@PathVariable String msisdn)
            throws SQLException, ClassNotFoundException {
        logger.info("Retrieving balance from Oracle for MSISDN: {}", msisdn);
        return ResponseEntity.ok(balanceService.retrieveBalanceFromOracle(msisdn));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCustomerBalance(@RequestBody CreateBalanceRequest request)
            throws SQLException, ClassNotFoundException {
        logger.info("Creating balance for customer: {}", request.customerId());
        return ResponseEntity.ok(balanceService.createCustomerBalance(request));
    }

    // Dakika güncelleme
    @PutMapping("/update/minutes")
    public ResponseEntity<String> updateMinutes(@RequestParam String msisdn, @RequestParam int minutes) {
        balanceService.updateBalanceMinutes(msisdn, minutes);
        return ResponseEntity.ok("Minutes updated for MSISDN: " + msisdn);
    }

    //  SMS güncelleme
    @PutMapping("/update/sms")
    public ResponseEntity<String> updateSms(@RequestParam String msisdn, @RequestParam int sms) {
        balanceService.updateBalanceSms(msisdn, sms);
        return ResponseEntity.ok("SMS updated for MSISDN: " + msisdn);
    }

    // Veri (data) güncelleme
    @PutMapping("/update/data")
    public ResponseEntity<String> updateData(@RequestParam String msisdn, @RequestParam int data) {
        balanceService.updateBalanceData(msisdn, data);
        return ResponseEntity.ok("Data updated for MSISDN: " + msisdn);
    }

    // Hepsini birden güncelleme
    @PutMapping("/update/all")
    public ResponseEntity<String> updateAll(@RequestBody BalanceUpdateRequest request) {
        balanceService.updateAllBalanceFields(request);
        return ResponseEntity.ok("All balance fields updated for MSISDN: " + request.msisdn());
    }

    @PostMapping("/insertBalance")
    public ResponseEntity<String> insertBalance(@RequestParam int customerId, @RequestParam int packageId) {
        try {
            balanceService.insertBalanceToCustomer(customerId, packageId);
            return ResponseEntity.ok("Balance inserted successfully.");
        } catch (Exception e) {
            logger.error("Failed to insert balance", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Insert failed.");
        }
    }

}
