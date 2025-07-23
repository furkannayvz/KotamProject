package com.i2i.intern.kotam.aom.controller;

import com.i2i.intern.kotam.aom.dto.request.BalanceRequestDTO;
import com.i2i.intern.kotam.aom.model.Balance;
import com.i2i.intern.kotam.aom.service.BalanceServiceVoltdb;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Optional;

@RestController
@RequestMapping("/api/balances")
public class BalanceController {

    private final BalanceServiceVoltdb balanceService;

    public BalanceController(BalanceServiceVoltdb balanceService) {
        this.balanceService = balanceService;
    }

    // GET /api/balances/{msisdn}
    @GetMapping("/{msisdn}")
    public ResponseEntity<Balance> getBalanceByMsisdn(@PathVariable String msisdn) {
        Optional<Balance> balance = balanceService.getBalanceByMsisdn(msisdn);
        return balance.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /api/balances
    @PostMapping
    public ResponseEntity<String> insertBalance(@RequestBody BalanceRequestDTO dto) {

        try {
            System.out.println("Gelen DTO:\n" + dto);

            Balance balance = convertToEntity(dto);

            System.out.println("Oluşturulan Balance Entity:\n" + balance);


        boolean success = balanceService.insertBalance(balance);

        return success
                ? ResponseEntity.ok("Bakiye eklendi.")
                : ResponseEntity.badRequest().body("Bakiye eklenemedi.");


        //return ResponseEntity.ok("DTO alındı ve entity oluşturuldu (test).");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Hata: " + e.getMessage());
        }
    }



    private Balance convertToEntity(BalanceRequestDTO dto) throws Exception {
        Balance balance = new Balance();
        balance.setBalanceId(dto.getBalanceId());
        balance.setMsisdn(dto.getMsisdn());
        balance.setPackageId(dto.getPackageId());
        balance.setLeftMinutes(dto.getLeftMinutes());
        balance.setLeftSms(dto.getLeftSms());
        balance.setLeftData(dto.getLeftData());
        /*
        if (dto.getsDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            balance.setsDate(new Timestamp(sdf.parse(dto.getsDate()).getTime()));
        } else {
            balance.setsDate(new Timestamp(System.currentTimeMillis()));
        }*/

        return balance;
    }

    //  PUT /api/balances/minutes?msisdn=...&minutes=...
    @PutMapping("/minutes")
    public ResponseEntity<String> updateMinutes(@RequestParam String msisdn,
                                                @RequestParam Long minutes) {
        boolean success = balanceService.updateMinutesBalance(msisdn, minutes);
        return success
                ? ResponseEntity.ok("Dakika güncellendi.")
                : ResponseEntity.badRequest().body("Güncelleme başarısız.");
    }

    //  PUT /api/balances/sms?msisdn=...&sms=...
    @PutMapping("/sms")
    public ResponseEntity<String> updateSms(@RequestParam String msisdn,
                                            @RequestParam Long sms) {
        boolean success = balanceService.updateSmsBalance(msisdn, sms);
        return success
                ? ResponseEntity.ok("SMS güncellendi.")
                : ResponseEntity.badRequest().body("Güncelleme başarısız.");
    }

    //  PUT /api/balances/data?msisdn=...&data=...
    @PutMapping("/data")
    public ResponseEntity<String> updateData(@RequestParam String msisdn,
                                             @RequestParam Long data) {
        boolean success = balanceService.updateDataBalance(msisdn, data);
        return success
                ? ResponseEntity.ok("Veri miktarı güncellendi.")
                : ResponseEntity.badRequest().body("Güncelleme başarısız.");
    }

    //  GET /api/balances/max-id
    @GetMapping("/max-id")
    public ResponseEntity<Long> getMaxBalanceId() {
        Optional<Long> maxId = balanceService.getMaxBalanceId();
        return maxId.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
