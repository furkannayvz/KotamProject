package com.i2i.intern.kotam.aom.controller;

import com.i2i.intern.kotam.aom.dto.request.LoginRequestDTO;
import com.i2i.intern.kotam.aom.dto.response.LoginResponseDTO;
import com.i2i.intern.kotam.aom.model.Customer;
import com.i2i.intern.kotam.aom.service.CustomerServiceOracle;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
@Tag(name = "Müşteri İşlemleri", description = "Müşteri kaydı, giriş ve sorgulama işlemleri")
public class CustomerController {

    private final CustomerServiceOracle customerService;

    public CustomerController(CustomerServiceOracle customerServiceOracle) {
        this.customerService = customerServiceOracle;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String msisdn,
                                           @RequestParam String name,
                                           @RequestParam String surname,
                                           @RequestParam String email,
                                           @RequestParam String password,
                                           @RequestParam String nationalId) {
        boolean success = customerService.insertCustomer(msisdn, name, surname, email, password, nationalId);
        return success ? ResponseEntity.ok("Kayıt başarılı.") : ResponseEntity.badRequest().body("Kayıt başarısız.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = customerService.authenticateCustomer(request.getMsisdn(), request.getPassword());
        return response == null
                ? ResponseEntity.status(401).body(null)
                : ResponseEntity.ok(response);
    }

    @GetMapping("/{msisdn}")
    public ResponseEntity<Customer> getCustomerByMsisdn(@PathVariable String msisdn) {
        Optional<Customer> customer = customerService.getCustomerByMsisdn(msisdn);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkExists(@RequestParam String email,
                                               @RequestParam String nationalId) {
        boolean exists = customerService.checkCustomerExistsByMailAndNationalId(email, nationalId);
        return ResponseEntity.ok(exists);
    }
}



