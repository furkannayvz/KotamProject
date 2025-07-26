package org.sk.i2i.evren.TGF.service;

import akka.actor.ActorRef;
import org.sk.i2i.evren.TGF.constants.TransType;
import org.sk.i2i.evren.TGF.management.DelayManager;
import org.sk.i2i.evren.TGF.management.StatsManager;
import org.sk.i2i.evren.TGF.trafficGenerators.TrafficGenerator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TrafficGeneratorService {

    private final Map<TransType, Thread> runningThreads = new HashMap<>();
    private final Map<TransType, TrafficGenerator> generators = new HashMap<>();

    public synchronized String startTraffic(TransType type, ActorRef actorRef, StatsManager statsManager, DelayManager delayManager) {
        if (runningThreads.containsKey(type)) {
            return type + " generator already running.";
        }

        TrafficGenerator generator = new TrafficGenerator(type, actorRef, statsManager, delayManager);
        Thread thread = new Thread(generator);
        thread.start();

        runningThreads.put(type, thread);
        generators.put(type, generator);

        return type + " generator started.";
    }

    public synchronized String stopTraffic(TransType type) {
        if (!runningThreads.containsKey(type)) {
            return type + " generator is not running.";
        }

        generators.get(type).stop();
        runningThreads.remove(type);
        generators.remove(type);

        return type + " generator stopped.";
    }

    public boolean isRunning(TransType type) {
        return runningThreads.containsKey(type);
    }
}
