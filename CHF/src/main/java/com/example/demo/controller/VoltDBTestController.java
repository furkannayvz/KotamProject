package com.example.demo.controller;

import com.example.demo.service.VoltDBRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/voltdb")
public class VoltDBTestController {
    
    @Autowired
    private VoltDBRestService voltDBRestService;
    
    @GetMapping("/test-connection")
    public ResponseEntity<String> testConnection() {
        boolean connected = voltDBRestService.testConnection();
        return ResponseEntity.ok(connected ? "VoltDB API connection successful" : "VoltDB API connection failed");
    }
    
    @GetMapping("/balance/{msisdn}")
    public ResponseEntity<Map<String, Integer>> getAllBalances(@PathVariable String msisdn) {
        Map<String, Integer> balances = voltDBRestService.getAllBalances(msisdn);
        return ResponseEntity.ok(balances);
    }
    
    @GetMapping("/balance-info/{msisdn}")
    public ResponseEntity<VoltDBRestService.BalanceInfo> getBalanceInfo(@PathVariable String msisdn) {
        VoltDBRestService.BalanceInfo balance = voltDBRestService.getBalanceInfo(msisdn);
        return ResponseEntity.ok(balance);
    }
    
    @GetMapping("/customer/{msisdn}")
    public ResponseEntity<VoltDBRestService.CustomerInfo> getCustomerInfo(@PathVariable String msisdn) {
        VoltDBRestService.CustomerInfo customer = voltDBRestService.getCustomerInfo(msisdn);
        return ResponseEntity.ok(customer);
    }
    
    @GetMapping("/package/{msisdn}")
    public ResponseEntity<VoltDBRestService.PackageInfo> getPackageInfo(@PathVariable String msisdn) {
        VoltDBRestService.PackageInfo packageInfo = voltDBRestService.getPackageInfo(msisdn);
        return ResponseEntity.ok(packageInfo);
    }
    
    @GetMapping("/balance/{msisdn}/{usageType}")
    public ResponseEntity<Integer> getSpecificBalance(
            @PathVariable String msisdn, 
            @PathVariable String usageType) {
        int balance = voltDBRestService.getRemainingBalance(msisdn, usageType);
        return ResponseEntity.ok(balance);
    }
    
    @PutMapping("/test-update-data/{msisdn}")
    public ResponseEntity<String> testUpdateData(
            @PathVariable String msisdn,
            @RequestParam int dataAmount) {
        boolean success = voltDBRestService.updateDataBalance(msisdn, dataAmount);
        return ResponseEntity.ok(success ? "Data balance updated" : "Failed to update data balance");
    }
    
    @PutMapping("/test-update-sms/{msisdn}")
    public ResponseEntity<String> testUpdateSms(
            @PathVariable String msisdn,
            @RequestParam int smsAmount) {
        boolean success = voltDBRestService.updateSmsBalance(msisdn, smsAmount);
        return ResponseEntity.ok(success ? "SMS balance updated" : "Failed to update SMS balance");
    }
    
    @PutMapping("/test-update-minutes/{msisdn}")
    public ResponseEntity<String> testUpdateMinutes(
            @PathVariable String msisdn,
            @RequestParam int minutesAmount) {
        boolean success = voltDBRestService.updateMinutesBalance(msisdn, minutesAmount);
        return ResponseEntity.ok(success ? "Minutes balance updated" : "Failed to update minutes balance");
    }
}
