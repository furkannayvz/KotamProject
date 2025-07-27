package org.sk.i2i.evren.TGF;

import akka.actor.*;
import com.typesafe.config.ConfigFactory;
import org.sk.i2i.evren.TGF.actors.AkkaActor;
import org.sk.i2i.evren.TGF.actors.DeadLetterListener;
import org.sk.i2i.evren.TGF.command.CommandHandler;
import org.sk.i2i.evren.TGF.constants.TransType;
import org.sk.i2i.evren.TGF.management.DelayManager;
import org.sk.i2i.evren.TGF.management.StatsManager;
import org.sk.i2i.evren.TGF.management.ThreadsManager;
import org.sk.i2i.evren.TGF.trafficGenerators.TrafficGenerator;
import org.sk.i2i.evren.TGF.service.CHFClientService;
import org.sk.i2i.evren.TGF.util.Clock;

public class Main {
    public static void main(String[] args) {
        
        String chfUrl = "http://13.60.49.99:8080";
        CHFClientService chfClient = new CHFClientService(chfUrl);
        System.out.println("CHF Client created for: " + chfUrl);
        
        StatsManager statsManager = new StatsManager();
        DelayManager delayManager = new DelayManager(3000000000L, 3000000000L, 3000000000L);
        
        ActorSystem actorSystem = ActorSystem.create("TGFSystem", ConfigFactory.load("application.conf"));
        ActorRef actor = actorSystem.actorOf(Props.create(AkkaActor.class), "TGFActor");
        ActorRef deadLetterListener = actorSystem.actorOf(Props.create(DeadLetterListener.class, statsManager), "deadLetterListener");
        
        actorSystem.eventStream().subscribe(deadLetterListener, DeadLetter.class);
        
        TrafficGenerator voice = new TrafficGenerator(TransType.VOICE, actor, statsManager, delayManager, chfClient);
        TrafficGenerator data = new TrafficGenerator(TransType.DATA, actor, statsManager, delayManager, chfClient);
        TrafficGenerator sms = new TrafficGenerator(TransType.SMS, actor, statsManager, delayManager, chfClient);
        
        ThreadsManager threadsManager = new ThreadsManager(voice, data, sms);
        
        CommandHandler commander = new CommandHandler(threadsManager, statsManager, delayManager);
        commander.startCommander();
        
        Clock.delay(1000000L);
        actorSystem.terminate();
        Clock.delay(1000L);
        System.exit(0);
    }
}
