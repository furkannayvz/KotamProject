package net.i2i.kotam;


import net.i2i.kotam.config.ConfigReader;
import net.i2i.kotam.kafka.KafkaConsumerService;
import net.i2i.kotam.kafka.KafkaProducerService;
import net.i2i.kotam.model.*;

public class KafkaApplication {



    public static void main(String[] args)  {
        String kafkaUrl = ConfigReader.get("kafka.url");
        String groupId = ConfigReader.get("kafka.group.id");


    
        System.out.println("\n--- Starting Kafka Consumer (will run in background for a short period) ---");
        KafkaConsumerService consumerService = new KafkaConsumerService(
                kafkaUrl,
                groupId,
                KafkaProducerService.USER_EVENTS_TOPIC,
                KafkaProducerService.USAGE_UPDATES_TOPIC,
                KafkaProducerService.BALANCE_UPDATES_TOPIC,
                KafkaProducerService.NOTIFICATION_EVENTS_TOPIC
        );

       
        Thread consumerThread = new Thread(consumerService);
        consumerThread.start();


    }
}
