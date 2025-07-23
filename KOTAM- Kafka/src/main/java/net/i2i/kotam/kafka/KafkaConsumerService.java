package net.i2i.kotam.kafka;

import java.util.Arrays;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.i2i.kotam.model.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;


public class KafkaConsumerService implements Runnable {

    private final Consumer<String, String> consumer;
    private final String[] topics;
    private final ObjectMapper objectMapper;

    public KafkaConsumerService(String bootstrapServers, String groupId, String... topics) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); 

     


        this.consumer = new KafkaConsumer<>(props);
        this.topics = topics;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    @Override
    public void run() {
        try {
            consumer.subscribe(Arrays.asList(topics));
            System.out.println("Consumer started, subscribed to topics: " + String.join(", ", topics));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("Received message from topic '" + record.topic() + "':");
                    System.out.println("  Key: " + record.key());
                    System.out.println("  Value: " + record.value());

                    
                    try {
                        switch (record.topic()) {
                            case KafkaProducerService.USER_EVENTS_TOPIC:
                                CustomerEvent customerEvent = objectMapper.readValue(record.value(), CustomerEvent.class);
                                System.out.println("  Deserialized CustomerEvent: " + customerEvent);
                                
                                break;
                            case KafkaProducerService.USAGE_UPDATES_TOPIC:
                                UsageEvent usageEvent = objectMapper.readValue(record.value(), UsageEvent.class);
                                System.out.println("  Deserialized UsageEvent: " + usageEvent);
                                
                                break;
                            case KafkaProducerService.BALANCE_UPDATES_TOPIC:
                                BalanceUpdate balanceUpdate = objectMapper.readValue(record.value(), BalanceUpdate.class);
                                System.out.println("  Deserialized BalanceUpdate: " + balanceUpdate);
                                
                                break;
                            case KafkaProducerService.NOTIFICATION_EVENTS_TOPIC:
                                try {
                                    NotificationMessage message = objectMapper.readValue(record.value(), NotificationMessage.class);
                                    System.out.println("  Deserialized NotificationMessage: " + message);
                                    
                                } catch (Exception e) {
                                    System.err.println("Failed to deserialize NotificationMessage: " + e.getMessage());
                                }
                                break;
                            default:
                                System.out.println("  Unknown topic, raw value: " + record.value());
                                break;
                        }
                    } catch (Exception e) {
                        System.err.println("Error deserializing message: " + e.getMessage());
                    }
                }
            }
        } catch (WakeupException e) {
            
        } catch (Exception e) {
            System.err.println("Consumer encountered an error: " + e.getMessage());
        } finally {
            consumer.close();
            System.out.println("Consumer closed.");
        }
    }

    public void shutdown() {
        consumer.wakeup(); 
    }
}
