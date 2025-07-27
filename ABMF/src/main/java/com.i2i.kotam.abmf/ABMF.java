package com.i2i.kotam.abmf;

// JSON işlemleri için Jackson kütüphaneleri
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// BalanceUpdate veri modelini import ediyoruz
import net.i2i.kotam.model.BalanceUpdate;

// Kafka consumer için gerekli sınıflar
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ABMF {

    private static final int MAX_RETRIES = 3; 
    // Bir mesaj işlenirken maksimum kaç defa deneneceğini tutar (hata olursa retry mekanizması)

    private final KafkaConsumer<String, String> consumer;
    // Kafka'dan mesajları çekmek için KafkaConsumer objesi

    private final ObjectMapper objectMapper;
    // JSON veriyi BalanceUpdate objesine dönüştürmek için Jackson ObjectMapper

    // Constructor - Kafka tüketici ayarları burada yapılır
    public ABMF(String bootstrapServers, String groupId, String topic) {
        Properties props = new Properties();

        // Kafka broker adresi
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // Kafka consumer grubunun ID'si
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        // Mesajın key ve value'sunun String olarak deserialize edilmesi
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // Offset sıfırdan yani en eski mesajdan başlayarak okunacak
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Otomatik commit devre dışı bırakıldı, commit manuel yapılacak
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        this.consumer = new KafkaConsumer<>(props);
        // Consumer oluşturulup belirlenen topic'e abone olunuyor
        consumer.subscribe(Collections.singletonList(topic));

        this.objectMapper = new ObjectMapper();
        // Java 8 tarih saat API'si için modül kayıt ediliyor (SDATE gibi alanlar için)
        objectMapper.registerModule(new JavaTimeModule());

        // JSON'da tanımlanmayan alanlar varsa hata fırlatma, onları yoksay
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void consumeMessages() {
        System.out.println("Kafka dinleniyor...");

        while (true) {
            // 1.5 saniyelik timeout ile Kafka'dan mesajları çek
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1500));

            if (records.isEmpty()) {
                // Eğer hiç mesaj yoksa, bilgi mesajı yazdır
                System.out.println("Kayıt alınamadı...");
                continue;
            }

            // Gelen her mesaj için işlemleri yap
            records.forEach(record -> {
                String json = record.value();

                // Mesaj boş veya geçersizse atla
                if (json == null || json.isEmpty()) {
                    System.err.println("Boş veya geçersiz mesaj atlandı. Offset: " + record.offset());
                    return;
                }

                int attempt = 0; // Mesaj işleme deneme sayacı
                boolean processed = false; // Mesaj başarıyla işlenip işlenmedi bilgisi

                // Mesajı en fazla MAX_RETRIES kadar işlemeye çalış
                while (!processed && attempt < MAX_RETRIES) {
                    try {
                        // JSON string'i BalanceUpdate objesine çevir
                        BalanceUpdate balanceUpdate = objectMapper.readValue(json, BalanceUpdate.class);

                        // Mesaj içeriğini konsola yazdır (debug/log amaçlı)
                        System.out.println("---- BalanceUpdate ----");
                        System.out.println("BALANCE_ID: " + balanceUpdate.getBALANCE_ID());
                        System.out.println("MSISDN: " + balanceUpdate.getMSISDN());
                        System.out.println("PACKAGE_ID: " + balanceUpdate.getPACKAGE_ID());
                        System.out.println("BAL_LEFT_MINUTES: " + balanceUpdate.getBAL_LEFT_MINUTES());
                        System.out.println("BAL_LEFT_SMS: " + balanceUpdate.getBAL_LEFT_SMS());
                        System.out.println("BAL_LEFT_DATA: " + balanceUpdate.getBAL_LEFT_DATA());
                        System.out.println("SDATE: " + balanceUpdate.getSDATE());
                        System.out.println("------------------------");

                        // İşlenen BalanceUpdate verisini Oracle veritabanına güncellemek için fonksiyon çağrısı
                        OracleOperations.updateUserBalance(balanceUpdate);

                        // İşlem başarılıysa Kafka offset commit edilir (manuel commit)
                        consumer.commitSync();
                        processed = true;
                    } catch (Exception e) {
                        // Hata olursa retry sayısını artır ve hata mesajını yazdır
                        attempt++;
                        System.err.println("Mesaj işlenirken hata (deneme " + attempt + "): " + e.getMessage());

                        // Maksimum deneme sayısına ulaşıldıysa hatayı logla ve mesajı atla
                        if (attempt == MAX_RETRIES) {
                            System.err.println("Maksimum deneme sayısına ulaşıldı. İşlenemeyen mesaj: " + json);
                        }
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        // Kafka broker IP ve port, consumer group ID ve topic adı sabit tanımlandı
        String bootstrapServers = "34.52.174.6:9092";
        String groupId = "abmf-consumer-group";
        String topic = "balanceUpdates";

        // ABMF consumer objesi oluşturulur ve consumeMessages metodu ile mesaj dinleme başlatılır
        ABMF abmfConsumer = new ABMF(bootstrapServers, groupId, topic);
        abmfConsumer.consumeMessages();
    }
}

