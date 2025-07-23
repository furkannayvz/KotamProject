/*

package com.i2i.intern.kotam.aom.controller;

import com.i2i.intern.kotam.aom.service.HazelcastService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hazelcast")
public class HazelcastController {

    private final HazelcastService hazelcastService;

    public HazelcastController(HazelcastService hazelcastService) {
        this.hazelcastService = hazelcastService;
    }

    @PostMapping("/send-msisdn")
    public ResponseEntity<String> sendMsisdn(
            @RequestParam String msisdn,
            @RequestParam(defaultValue = "active") String value) {

        String result = hazelcastService.sendMsisdnToHazelcast(msisdn, value);
        return ResponseEntity.ok(result);
    }
}

 */
