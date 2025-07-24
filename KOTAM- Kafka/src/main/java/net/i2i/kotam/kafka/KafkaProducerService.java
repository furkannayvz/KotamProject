package net.i2i.kotam.kafka;



import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.i2i.kotam.model.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaProducerService {

    private final Producer<String, String> producer;
    private final ObjectMapper objectMapper;

    // Kafka Topic Names
    public static final String USER_EVENTS_TOPIC = "userEvents";
    public static final String USAGE_UPDATES_TOPIC = "usageUpdates";
    public static final String BALANCE_UPDATES_TOPIC = "balanceUpdates";
    public static final String NOTIFICATION_EVENTS_TOPIC = "notificationEvents";

    public KafkaProducerService(String bootstrapServers) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // Ensure all replicas acknowledge the message
        props.put(ProducerConfig.RETRIES_CONFIG, 3);

        // Security settings for Google Cloud Kafka (e.g., Confluent Cloud)
        // props.put("security.protocol", "SASL_SSL");
        // props.put("sasl.mechanism", "PLAIN");
        // props.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"YOUR_KAFKA_API_KEY\" password=\"YOUR_KAFKA_API_SECRET\";");

        this.producer = new KafkaProducer<>(props);

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    }

    public <T> void publishMessage(String topic, String key, T value) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, jsonValue);

            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        System.out.println("Message delivered to topic '" + metadata.topic() +
                                "' [" + metadata.partition() +
                                "] at offset " + metadata.offset());
                    } else {
                        System.err.println("Message delivery failed: " + exception.getMessage());
                    }
                }
            }).get(); // Use .get() to make it synchronous for simple examples,
            // but in production, prefer asynchronous handling.

        } catch (ExecutionException | InterruptedException e) {
            System.err.println("Error producing message: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Error serializing message or publishing: " + e.getMessage());
        }
    }

    public void close() {
        producer.close();
    }
}
