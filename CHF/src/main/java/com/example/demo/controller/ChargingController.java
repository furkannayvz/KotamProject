package com.example.demo.controller;

import com.example.demo.dto.UsageDTO;
import com.example.demo.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usage")
public class ChargingController {

    @Autowired
    private KafkaService kafkaService;

    @PostMapping("/submit")
    public ResponseEntity<String> receiveUsage(@RequestBody UsageDTO dto) {
        try {
            System.out.println("kullanim verisi alindi: " + dto);
            
            kafkaService.sendUsageData(dto);
            
            return ResponseEntity.ok("Veri başariyla alindi ve Kafka'ya gönderildi");
            
        } catch (Exception e) {
            System.err.println("Hata: " + e.getMessage());
            return ResponseEntity.internalServerError()
                .body("Veri işlenirken hata oluştu: " + e.getMessage());
        }
    }
}
