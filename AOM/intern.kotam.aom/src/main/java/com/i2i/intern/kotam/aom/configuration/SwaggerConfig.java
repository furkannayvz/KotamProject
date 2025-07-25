package com.i2i.intern.kotam.aom.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("KOTAM API")
                        .version("1.0")
                        .description("Kullanıcı yönetimi ve paket işlemleri için API dokümantasyonu"));
    }
}
