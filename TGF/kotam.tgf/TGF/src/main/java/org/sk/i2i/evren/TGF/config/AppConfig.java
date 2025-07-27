package org.sk.i2i.evren.TGF.config;

import akka.actor.*;
import com.typesafe.config.ConfigFactory;
import org.sk.i2i.evren.TGF.actors.AkkaActor;
import org.sk.i2i.evren.TGF.actors.DeadLetterListener;
import org.sk.i2i.evren.TGF.management.DelayManager;
import org.sk.i2i.evren.TGF.management.StatsManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public StatsManager statsManager() {
        return new StatsManager();
    }

    @Bean
    public DelayManager delayManager() {
        return new DelayManager(3000000000L, 3000000000L, 3000000000L);
    }

    @Bean
    public ActorSystem actorSystem(StatsManager statsManager) {
        ActorSystem system = ActorSystem.create("TGFSystem", ConfigFactory.load("application.conf"));
        ActorRef deadLetterListener = system.actorOf(Props.create(DeadLetterListener.class, statsManager), "deadLetterListener");
        system.eventStream().subscribe(deadLetterListener, DeadLetter.class);
        return system;
    }

    @Bean
    public ActorRef actorRef(ActorSystem actorSystem) {
        return actorSystem.actorOf(Props.create(AkkaActor.class), "TGFActor");
    }
}
