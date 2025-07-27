package org.sk.i2i.evren.TGF.trafficGenerators;

import akka.actor.ActorRef;
import org.sk.i2i.evren.TGF.dto.DataTransaction;
import org.sk.i2i.evren.TGF.dto.SmsTransaction;
import org.sk.i2i.evren.TGF.constants.TransType;
import org.sk.i2i.evren.TGF.management.DelayManager;
import org.sk.i2i.evren.TGF.management.StatsManager;
import org.sk.i2i.evren.TGF.util.Clock;
import org.sk.i2i.evren.TGF.util.RandomGenerator;
import org.sk.i2i.evren.TGF.dto.VoiceTransaction;
import org.sk.i2i.evren.TGF.service.CHFClientService;

public class TrafficGenerator implements Runnable{
    private final TransType type;
    private final ActorRef actor;
    private boolean isGenerate = true;
    private final StatsManager statsManager;
    private final DelayManager delayManager;
    private final CHFClientService chfClient;

    /**
     * @param type type of transaction to be generated
     * @param actor akka actor which will send transactions
     * @param delayManager manages the delay time between transactions
     * @param statsManager manages stats of generator, counts transactions and dropped transactions
     * @param chfClient client to send HTTP requests to CHF (can be null)
     */
    public TrafficGenerator(TransType type, ActorRef actor, StatsManager statsManager, DelayManager delayManager, CHFClientService chfClient) {
        this.type = type;
        this.actor = actor;
        this.statsManager = statsManager;
        this.delayManager = delayManager;
        this.chfClient = chfClient;
    }

    public TrafficGenerator(TransType type, ActorRef actor, StatsManager statsManager, DelayManager delayManager) {
        this(type, actor, statsManager, delayManager, null);
    }

    @Override
    public void run() {
        statsManager.resetStats();
        isGenerate = true;

        while(isGenerate) {
            sendTransaction();
            statsManager.incrementCounter(type);
            Clock.delay(delayManager.getDelay(type));
        }

        System.out.println(type + " traffic generation stopped...");
    }

    private void sendTransaction() {
        String msisdn = RandomGenerator.randomMsisdn();
        int location = RandomGenerator.randomLocation();

        switch (type) {
            case DATA -> {
                DataTransaction trans = new DataTransaction(
                        msisdn,
                        location,
                        RandomGenerator.randomDataUsage(),
                        RandomGenerator.randomRatingGroup());
                
                actor.tell(trans, ActorRef.noSender());
                
                if (chfClient != null) {
                    chfClient.sendDataCharging(trans);
                }
            }
            case VOICE -> {
                VoiceTransaction trans =  new VoiceTransaction(
                        msisdn,
                        RandomGenerator.randomMsisdn(),
                        location,
                        RandomGenerator.randomDuration()
                );
                
                actor.tell(trans, ActorRef.noSender());
                
                if (chfClient != null) {
                    chfClient.sendVoiceCharging(trans);
                }
            }
            case SMS -> {
                SmsTransaction trans = new SmsTransaction(
                        msisdn,
                        RandomGenerator.randomMsisdn(),
                        location
                );
                
                actor.tell(trans, ActorRef.noSender());
                
                if (chfClient != null) {
                    chfClient.sendSmsCharging(trans);
                }
            }
        }
    }

    public void stop() {
        this.isGenerate = false;
    }
}
