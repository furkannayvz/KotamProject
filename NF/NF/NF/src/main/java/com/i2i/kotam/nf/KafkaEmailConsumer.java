package com.i2i.kotam.nf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.i2i.kotam.model.NotificationMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import javax.mail.Session;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaEmailConsumer {

    public static void main(String[] args) throws SQLException {
        new KafkaEmailConsumer().run();
    }

    public void run() throws SQLException {
        // Kafka Ayarları
        Properties props = new Properties();
        props.put("bootstrap.servers", ConfigLoader.getProperty("kafka.url"));
        props.put("group.id", "notification-group");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("auto.offset.reset", "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("notificationEvents"));

        NotificationOperations mailService = new NotificationOperations();
        OracleOperations oracleService = new OracleOperations();
        Session session = mailService.createMailSession();
        ObjectMapper mapper = new ObjectMapper();

        System.out.println("Kafka dinleniyor...");

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                try {
                    // JSON string -> NotificationMessage nesnesi
                    NotificationMessage message = mapper.readValue(record.value(), NotificationMessage.class);

                    // Mail gönder
                    mailService.sendMail(session, message);

                    // Oracle prosedür çağır
                    oracleService.callInsertProcedure(message);
                } catch (Exception e) {
                    System.err.println("Mesaj işlenirken hata: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
