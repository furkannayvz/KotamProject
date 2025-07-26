package org.sk.i2i.evren.TGF.controller;

import org.sk.i2i.evren.TGF.util.RandomGenerator;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/random")
public class RandomGeneratorController {



    @GetMapping("/location")
    public int getRandomLocation() {
        return RandomGenerator.randomLocation();
    }

    @GetMapping("/data-usage")
    public int getRandomDataUsage() {
        return RandomGenerator.randomDataUsage();
    }

    @GetMapping("/rating-group")
    public int getRandomRatingGroup() {
        return RandomGenerator.randomRatingGroup();
    }

    @GetMapping("/duration")
    public int getRandomDuration() {
        return RandomGenerator.randomDuration();
    }

    @GetMapping("/msisdn")
    public String getRandomMsisdn() {
        return RandomGenerator.randomMsisdn();
    }

    @PostMapping("/msisdn/fetch")
    public String fetchMsisdnList() {
        try {
            RandomGenerator.fetchMsisdn();
            return "MSISDN listesi güncellendi.";
        } catch (Exception e) {
            e.printStackTrace();  // log'a yaz
            return "Hata: " + e.getMessage();
        }
    }

}
