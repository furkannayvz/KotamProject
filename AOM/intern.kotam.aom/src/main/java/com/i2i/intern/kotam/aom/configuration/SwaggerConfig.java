package com.i2i.intern.kotam.aom.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Kotam AOM Projesi için OpenAPI konfigürasyonu
 * Swagger arayüzü /swagger-ui.html adresinden erişilebilir
 */
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "GitHub Proje Linki",
                        url = "https://github.com/furkannayvz/KotamProject",
                        email = "zelihapolat111@gmail.com"
                ),
                description = "Kotam AOM projesi için geliştirilen REST API dökümantasyonu.",
                title = "Kotam AOM - API Dokümantasyonu",
                version = "1.0"
        )
)
@Configuration
public class SwaggerConfig { }
