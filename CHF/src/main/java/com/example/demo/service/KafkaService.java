package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${chf.kafka.topic.usage-data:usage-data}")
    private String usageDataTopic;

    public void sendUsageData(Object usageData) {
        try {
            kafkaTemplate.send(usageDataTopic, usageData);
            logger.info("Kafka'ya veri gönderildi: {}", usageData);
        } catch (Exception e) {
            logger.error("Kafka'ya veri gönderilirken hata: {}", e.getMessage());
            throw new RuntimeException("Kafka'ya veri gönderilemedi", e);
        }
    }
}
