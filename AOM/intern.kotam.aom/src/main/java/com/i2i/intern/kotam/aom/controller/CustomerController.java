package com.i2i.intern.kotam.aom.controller;

import com.i2i.intern.kotam.aom.model.Customer;
import com.i2i.intern.kotam.aom.service.CustomerServiceOracle;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerServiceOracle customerService;

    public CustomerController(CustomerServiceOracle customerService) {
        this.customerService = customerService;
    }

    // === KAYIT ===
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String msisdn,
                                           @RequestParam String name,
                                           @RequestParam String surname,
                                           @RequestParam String email,
                                           @RequestParam String password,
                                           @RequestParam String nationalId) {
        boolean success = customerService.insertCustomer(msisdn, name, surname, email, password, nationalId);
        return success
                ? ResponseEntity.ok("Kayıt başarılı.")
                : ResponseEntity.badRequest().body("Kayıt başarısız.");
    }

    // === GİRİŞ ===
    @PostMapping("/login")
    public ResponseEntity<Customer> login(@RequestParam String msisdn,
                                          @RequestParam String password) {
        Optional<Customer> customer = customerService.authenticateCustomer(msisdn, password);
        return customer
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).build());
    }

    // === ŞİFRE SIFIRLAMA ===
    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email,
                                                @RequestParam String nationalId,
                                                @RequestParam String newPassword) {
        boolean result = customerService.resetPassword(email, nationalId, newPassword);
        return result
                ? ResponseEntity.ok("Şifre güncellendi.")
                : ResponseEntity.badRequest().body("Şifre güncellenemedi.");
    }

    // === ŞİFREMİ UNUTTUM ===
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email,
                                                 @RequestParam String nationalId) {
        boolean exists = customerService.forgotPassword(email, nationalId);
        return exists
                ? ResponseEntity.ok("Şifre sıfırlama işlemi mümkün.")
                : ResponseEntity.badRequest().body("Böyle bir kullanıcı bulunamadı.");
    }

    // === MSISDN İLE KULLANICIYI GETİR ===
    @GetMapping("/{msisdn}")
    public ResponseEntity<Customer> getCustomerByMsisdn(@PathVariable String msisdn) {
        Optional<Customer> customer = customerService.getCustomerByMsisdn(msisdn);
        return customer
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // === MEVCUT MU DİYE KONTROL ===
    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkExists(@RequestParam String email,
                                               @RequestParam String nationalId) {
        boolean exists = customerService.checkCustomerExistsByMailAndNationalId(email, nationalId);
        return ResponseEntity.ok(exists);
    }
}
