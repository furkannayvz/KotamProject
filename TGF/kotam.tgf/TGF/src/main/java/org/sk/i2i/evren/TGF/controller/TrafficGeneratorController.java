package org.sk.i2i.evren.TGF.controller;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import jakarta.annotation.PostConstruct;
import org.sk.i2i.evren.TGF.actors.AkkaActor;
import org.sk.i2i.evren.TGF.config.TrafficDelayConfig;
import org.sk.i2i.evren.TGF.constants.TransType;
import org.sk.i2i.evren.TGF.management.DelayManager;
import org.sk.i2i.evren.TGF.management.StatsManager;
import org.sk.i2i.evren.TGF.service.TrafficGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/traffic")
public class TrafficGeneratorController {

    private final TrafficGeneratorService trafficService;
    private final ActorRef actorRef;
    private final StatsManager statsManager;
    private final DelayManager delayManager;

    public TrafficGeneratorController(
            TrafficGeneratorService trafficService,
            ActorRef actorRef,
            StatsManager statsManager,
            DelayManager delayManager
    ) {
        this.trafficService = trafficService;
        this.actorRef = actorRef;
        this.statsManager = statsManager;
        this.delayManager = delayManager;
    }

    @PostMapping("/start/{type}")
    public String start(@PathVariable TransType type) {
        return trafficService.startTraffic(type, actorRef, statsManager, delayManager);
    }

    @PostMapping("/stop/{type}")
    public String stop(@PathVariable TransType type) {
        return trafficService.stopTraffic(type);
    }

    @GetMapping("/status/{type}")
    public String status(@PathVariable TransType type) {
        boolean running = trafficService.isRunning(type);
        return type + " generator is " + (running ? "running" : "not running");
    }
}
