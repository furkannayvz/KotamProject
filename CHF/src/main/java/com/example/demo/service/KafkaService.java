package com.example.demo.service;

import com.example.demo.dto.NotificationMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);
    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    private static final String NOTIFICATION_TOPIC = "notificationEvents";

    public KafkaService(KafkaTemplate<String, NotificationMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    //sends a notification message to the Kafka NOTIFICATION_EVENTS_TOPIC.
    //The key for the Kafka message will be the MSISDN.
    public void sendNotificationEvent(NotificationMessage notificationMessage) {
        try {
            kafkaTemplate.send(NOTIFICATION_TOPIC, notificationMessage.getMsisdn(), notificationMessage)
                         .whenComplete((result, ex) -> {
                             if (ex == null) {
                                 logger.info("Sent notification event to topic '{}' for MSISDN '{}', offset {}",
                                         NOTIFICATION_TOPIC, notificationMessage.getMsisdn(), result.getRecordMetadata().offset());
                             } else {
                                 logger.error("Failed to send notification event for MSISDN '{}' to topic '{}': {}",
                                         notificationMessage.getMsisdn(), NOTIFICATION_TOPIC, ex.getMessage());
                             }
                         });
        } catch (Exception e) {
            logger.error("Error while attempting to send notification event for MSISDN '{}': {}",
                    notificationMessage.getMsisdn(), e.getMessage(), e);
        }
    }
}
