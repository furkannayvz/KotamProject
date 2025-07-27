package com.i2i.kotam.abmf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    // application.properties dosyasından yüklenen tüm konfigürasyonları tutan Properties nesnesi
    private static final Properties properties = new Properties();

    // Statik blok - sınıf ilk yüklendiğinde sadece bir kere çalışır
    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("application.properties")) {
            // application.properties dosyasını classpath'ten bul ve InputStream olarak aç
            if (input == null) {
                // Dosya bulunamazsa programın çalışmasını durdurmak için RuntimeException fırlatılır
                throw new RuntimeException("Üzgünüz, application.properties dosyası bulunamadı");
            }
            // Dosyadaki özellikler (key=value) Properties objesine yüklenir
            properties.load(input);
        } catch (IOException ex) {
            // Dosya okuma sırasında bir hata olursa programı durduracak exception fırlatılır
            throw new RuntimeException("Özellikler dosyası yüklenirken hata oluştu", ex);
        }
    }

    // Belirtilen key'e karşılık gelen değeri döndüren statik metot
    public static String getProperty(String key) {
        // Öncelikle ortam değişkenlerinde (environment variables) key'in büyük harfli ve nokta yerine alt çizgi ile yazılmış hali aranır
        // Örneğin "db.username" -> "DB_USERNAME"
        String envValue = System.getenv(key.toUpperCase().replace(".", "_"));
        if (envValue != null) {
            // Eğer ortam değişkeninde değer varsa onu öncelikli olarak döndür
            return envValue;
        }
        // Ortam değişkeninde yoksa application.properties dosyasındaki değeri döndür
        return properties.getProperty(key);
    }
}

