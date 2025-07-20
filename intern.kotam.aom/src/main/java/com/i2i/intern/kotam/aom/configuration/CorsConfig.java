package com.i2i.intern.kotam.aom.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    /**
     * CORS filter yapılandırması
     * Belirli origin'e (örn: Swagger veya frontend) izin verir ve credential'ları destekler
     * @return CorsFilter
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // Kimlik doğrulama bilgilerini (JWT, cookie) göndermeye izin ver
        corsConfig.setAllowCredentials(true);

        // Sadece belirli origin'lere izin ver — '*' kullanılamaz!
        corsConfig.setAllowedOrigins(List.of("http://localhost:8080"));

        // Tüm header'lara izin ver
        corsConfig.addAllowedHeader("*");

        // Tüm HTTP method'lara (GET, POST, PUT, DELETE vs.) izin ver
        corsConfig.addAllowedMethod("*");

        // URL'lere bu ayarları uygula
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsFilter(source);
    }
}
