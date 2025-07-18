package com.voltdb.controller;

import com.voltdb.dto.CustomerDTO;
import com.voltdb.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @Operation(
            summary = "Kullanıcı ekle",
            description = "Verilen bilgilere sahip kullanıcıyı tabloya ekler"
    )
    public ResponseEntity<String> createCustomer(@RequestBody CustomerDTO dto) throws Exception {
        customerService.insertCustomer(dto);
        return ResponseEntity.ok("Customer created.");
    }

    @GetMapping("/{msisdn}")
    @Operation(
            summary = "Kullanıcı bul",
            description = "Belirtilen MSISDN'ye sahip kullanıcıyı getirir."
    )
    public ResponseEntity<CustomerDTO> getCustomerByMsisdn(@PathVariable String msisdn) throws Exception {
        CustomerDTO customer = customerService.getCustomerInfoByMsisdn(msisdn);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/password")
    @Operation(
            summary = "Şifre güncelle",
            description = "Belirtilen email ve nationalId ye sahip kullanıcının şifresini değiştirir."
    )
    public ResponseEntity<String> updatePassword(
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String nationalId) throws Exception {

        customerService.updateCustomerPassword(password, email, nationalId);
        return ResponseEntity.ok("Password updated.");
    }

    @GetMapping("/exists")
    @Operation(
            summary = "Kullanıcı var mı",
            description = "Belirtilen email ve nationalID ye sahip bir kullacı var mı kontrolü yapar."
    )
    public ResponseEntity<Integer> checkCustomer(
            @RequestParam String email,
            @RequestParam String national_id) throws Exception{
        int value = customerService.checkIfCustomerExists(email,national_id);
        return ResponseEntity.ok(value);
    }

    @GetMapping("/password")
    @Operation(
            summary = "Şifre getir",
            description = "MSISDN sahibi kullanıcının şifresini getirir."
    )
    public ResponseEntity<String> getPassword(
            @RequestParam String msisdn) throws Exception{
        String value = customerService.getCustomerPasswordByMsisdn(msisdn);
        return ResponseEntity.ok(value);
    }

    @GetMapping("/email")
    @Operation(
            summary = "Email getir",
            description = "MSISDN sahibi kullanıcının emailini getirir."
    )
    public ResponseEntity<String> getEmail(
            @RequestParam String msisdn) throws Exception{
        String value = customerService.getCustomerEmailByMsisdn(msisdn);
        return ResponseEntity.ok(value);
    }

    @GetMapping("/max-msisdn")
    @Operation(
            summary = "Max msisdn getir",
            description = "Customer tablosunda bulunan en büyük msisdn'i getirir."
    )
    public ResponseEntity<String> getMaxMsisdn() throws Exception{
        String value = customerService.getMaxCustomerMsisdn();
        return ResponseEntity.ok(value);
    }

}
