package org.i2i.kotam;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.i2i.kotam.utils.configurations.Configuration; // Dizin yapınıza göre bu import'u kontrol edin

public class Main {
    public static void main(String[] args) {
        System.out.println("KOTAM Client Application Starting...");
        HazelcastInstance hazelcastClient = null;

        try {
            // 1. Configuration sınıfından doğru metodu çağırarak yapılandırmayı al.
            ClientConfig clientConfig = Configuration.getConfig();

            // 2. Alınan bu yapılandırma ile Hazelcast istemcisini başlat.
            hazelcastClient = HazelcastClient.newHazelcastClient(clientConfig);

            System.out.println("✅ Hazelcast client connected successfully!");

             // Test verisi
            hazelcastClient.getMap("msisdn-map").put("msisdn:905555000000", "active");
            System.out.println("Veri kontrol: " + hazelcastClient.getMap("msisdn-map").get("msisdn:905555000000"));

        } catch (Exception e) {
            System.err.println("❌ An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 3. İşlem bittiğinde istemciyi güvenli bir şekilde kapat.
            if (hazelcastClient != null) {
                hazelcastClient.shutdown();
                System.out.println("\nHazelcast client has been shut down.");
            }
        }
        System.out.println("KOTAM Client Application finished.");
    }
}