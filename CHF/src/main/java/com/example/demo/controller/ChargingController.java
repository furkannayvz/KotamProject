package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.ChargingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/charging")
public class ChargingController {

    @Autowired
    private ChargingService chargingService;

    /**
     * SMS Charging Endpoint
     * POST /charging/sms
     */
    @PostMapping("/sms")
    public Mono<ResponseEntity<ChargingResponse>> chargeSms(@RequestBody SmsChargingRequest request) {
        // Validate request
        if (request.getSenderMsisdn() == null || request.getSenderMsisdn().trim().isEmpty()) {
            ChargingResponse errorResponse = new ChargingResponse(false, "Sender MSISDN is required", null, "SMS");
            return Mono.just(ResponseEntity.badRequest().body(errorResponse));
        }

        return chargingService.processSmsCharging(request)
                .map(response -> {
                    if (response.isSuccess()) {
                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.badRequest().body(response);
                    }
                });
    }

    /**
     * Voice Charging Endpoint
     * POST /charging/voice
     */
    @PostMapping("/voice")
    public Mono<ResponseEntity<ChargingResponse>> chargeVoice(@RequestBody VoiceChargingRequest request) {
        if (request.getCallerMsisdn() == null || request.getCallerMsisdn().trim().isEmpty()) {
            ChargingResponse errorResponse = new ChargingResponse(false, "Caller MSISDN is required", null, "VOICE");
            return Mono.just(ResponseEntity.badRequest().body(errorResponse));
        }

        if (request.getDuration() <= 0) {
            ChargingResponse errorResponse = new ChargingResponse(false, "Duration must be greater than 0", request.getCallerMsisdn(), "VOICE");
            return Mono.just(ResponseEntity.badRequest().body(errorResponse));
        }

        return chargingService.processVoiceCharging(request)
                .map(response -> {
                    if (response.isSuccess()) {
                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.badRequest().body(response);
                    }
                });
    }

    /**
     * Data Charging Endpoint
     * POST /charging/data
     */
    @PostMapping("/data")
    public Mono<ResponseEntity<ChargingResponse>> chargeData(@RequestBody DataChargingRequest request) {
        if (request.getMsisdn() == null || request.getMsisdn().trim().isEmpty()) {
            ChargingResponse errorResponse = new ChargingResponse(false, "MSISDN is required", null, "DATA");
            return Mono.just(ResponseEntity.badRequest().body(errorResponse));
        }

        if (request.getDataUsage() <= 0) {
            ChargingResponse errorResponse = new ChargingResponse(false, "Data usage must be greater than 0", request.getMsisdn(), "DATA");
            return Mono.just(ResponseEntity.badRequest().body(errorResponse));
        }

        return chargingService.processDataCharging(request)
                .map(response -> {
                    if (response.isSuccess()) {
                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.badRequest().body(response);
                    }
                });
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Charging service is running");
    }
}
