package com.voltdb.controller;

import com.voltdb.dto.BalanceDTO;
import com.voltdb.dto.CustomerDTO;
import com.voltdb.dto.PackageDTO;
import com.voltdb.service.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balances")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/{msisdn}")
    @Operation(
            summary = "Balance bilgisi",
            description = "Verilen MSISDN ile ilişkili Balance bilgisini getirir."
    )
    public ResponseEntity<BalanceDTO> getBalanceInfoByMsisdn(@PathVariable String msisdn) throws Exception{
        BalanceDTO dto = balanceService.getBalanceInfoByMsisdn(msisdn);

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Operation(
            summary = "Balance ekle",
            description = "Verilen bilgilere sahip balance'ı tabloya ekler"
    )
    public ResponseEntity<String> insertBalance(@RequestBody BalanceDTO dto) throws Exception {
        balanceService.insertBalance(dto);
        return ResponseEntity.ok("Balance created.");
    }

    @PutMapping("/minutes")
    @Operation(
            summary = "Left Minutes güncelle",
            description = "Verilen minutes miktarını verilen msisdn'ye ait balance'da günceller"
    )
    public ResponseEntity<String>updateBalanceMinutesByMsisdn(
            @RequestParam int minutes,
            @RequestParam String msisdn) throws Exception {

        balanceService.updateBalanceMinutesByMsisdn(minutes,msisdn);
        return ResponseEntity.ok("Balance updated.");
    }

    @PutMapping("/sms")
    @Operation(
            summary = "Left Sms güncelle",
            description = "Verilen sms miktarını verilen msisdn'ye ait balance'da günceller"
    )
    public ResponseEntity<String>updateBalanceSmsByMsisdn(
            @RequestParam int sms,
            @RequestParam String msisdn) throws Exception {

        balanceService.updateBalanceSmsByMsisdn(sms,msisdn);
        return ResponseEntity.ok("Balance updated.");
    }

    @PutMapping("/data")
    @Operation(
            summary = "Left Data güncelle",
            description = "Verilen data miktarını verilen msisdn'ye ait balance'da günceller"
    )
    public ResponseEntity<String>updateBalanceDataByMsisdn(
            @RequestParam int data,
            @RequestParam String msisdn) throws Exception {

        balanceService.updateBalanceDataByMsisdn(data,msisdn);
        return ResponseEntity.ok("Balance updated.");
    }

    @GetMapping("/max-Id")
    @Operation(
            summary = "Max Balance Id getir",
            description = "Balance tablosunda bulunan en büyük Balance Id'yi getirir."
    )
    public ResponseEntity<Integer> getMaxBalanceId() throws Exception{
        Integer value = balanceService.getMaxBalanceId();
        return ResponseEntity.ok(value);
    }


}
