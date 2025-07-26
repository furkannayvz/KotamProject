package com.example.demo.controller;

import com.example.demo.service.VoltDBRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("")
public class VoltDBController {

    @Autowired
    private VoltDBRestService voltDBRestService;

    @GetMapping("/connection-test")
    public String testConnection() {
        boolean isConnected = voltDBRestService.testConnection();
        String connectionInfo = voltDBRestService.getConnectionInfo();
        
        return "Connection Status: " + (isConnected ? "SUCCESS" : "FAILED") + 
               "\nConnection Info: " + connectionInfo;
    }

    @GetMapping("/max-msisdn")
    public Mono<String> getMaxMsisdn() {
        return voltDBRestService.getMaxMsisdn();
    }

    @GetMapping("/customer/{msisdn}")
    public Mono<String> getCustomer(@PathVariable String msisdn) {
        return voltDBRestService.getCustomerByMsisdn(msisdn);
    }

    @GetMapping("/customer/email")
    public Mono<String> getCustomerEmail(@RequestParam String msisdn) {
        return voltDBRestService.getCustomerEmail(msisdn);
    }

    @GetMapping("/balance/{msisdn}")
    public Mono<String> getBalance(@PathVariable String msisdn) {
        return voltDBRestService.getBalanceByMsisdn(msisdn);
    }

    @GetMapping("/package/{msisdn}")
    public Mono<String> getPackage(@PathVariable String msisdn) {
        return voltDBRestService.getPackageByMsisdn(msisdn);
    }

    @GetMapping("/package/name/{msisdn}")
    public Mono<String> getPackageName(@PathVariable String msisdn) {
        return voltDBRestService.getPackageNameByMsisdn(msisdn);
    }

    @PutMapping("/balance/sms")
    public Mono<String> updateSms(@RequestParam String msisdn, @RequestParam int sms) {
        return voltDBRestService.updateSmsBalance(msisdn, sms);
    }

    @PutMapping("/balance/minutes")
    public Mono<String> updateMinutes(@RequestParam String msisdn, @RequestParam int minutes) {
        return voltDBRestService.updateMinutesBalance(msisdn, minutes);
    }

    @PutMapping("/balance/data")
    public Mono<String> updateData(@RequestParam String msisdn, @RequestParam int data) {
        return voltDBRestService.updateDataBalance(msisdn, data);
    }
}
