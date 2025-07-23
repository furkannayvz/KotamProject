package net.i2i.kotam.kafka;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.i2i.kotam.handler.*;
import net.i2i.kotam.model.*;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

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
            logger.info("Consumer started, subscribed to topics: {}", String.join(", ", topics));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    String topic = record.topic();
                    String value = record.value();

                    logger.info("Received message from topic '{}'", topic);
                    logger.debug("  Key: {}", record.key());
                    logger.debug("  Value: {}", value);

                    try {
                        switch (topic) {
                            case KafkaProducerService.USER_EVENTS_TOPIC -> {
                                CustomerEvent customerEvent = objectMapper.readValue(value, CustomerEvent.class);
                                new CustomerEventHandler().handle(customerEvent);
                            }
                            case KafkaProducerService.USAGE_UPDATES_TOPIC -> {
                                UsageEvent usageEvent = objectMapper.readValue(value, UsageEvent.class);
                                new UsageEventHandler().handle(usageEvent);
                            }
                            case KafkaProducerService.BALANCE_UPDATES_TOPIC -> {
                                BalanceUpdate balanceUpdate = objectMapper.readValue(value, BalanceUpdate.class);
                                new BalanceUpdateHandler().handle(balanceUpdate);
                            }
                            case KafkaProducerService.NOTIFICATION_EVENTS_TOPIC -> {
                                NotificationMessage notification = objectMapper.readValue(value, NotificationMessage.class);
                                new NotificationMessageHandler().handle(notification);
                            }
                            default -> logger.warn("Unknown topic: '{}'. Raw value: {}", topic, value);
                        }
                    } catch (Exception e) {
                        logger.error("Failed to deserialize message from topic '{}': {}", topic, e.getMessage());
                    }
                }
            }

        } catch (WakeupException e) {
            // Expected on shutdown
        } catch (Exception e) {
            logger.error("Consumer encountered an error: {}", e.getMessage(), e);
        } finally {
            consumer.close();
            logger.info("Consumer closed.");
        }
    }

    public void shutdown() {
        consumer.wakeup();
    }
}
