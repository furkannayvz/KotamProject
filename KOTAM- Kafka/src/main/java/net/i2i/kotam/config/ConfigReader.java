package net.i2i.kotam.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        loadPropertiesFromFile();
    }

    private static void loadPropertiesFromFile() {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("application.properties not found in classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }

    // asıl kullanım için
    public static String get(String key) {
        return properties.getProperty(key);
    }

    // test için properties dışarıdan set edilsin
    public static void setProperties(Properties props) {
        properties = props;
    }
}
