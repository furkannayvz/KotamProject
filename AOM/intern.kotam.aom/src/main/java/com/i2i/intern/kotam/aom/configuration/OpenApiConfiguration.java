package com.i2i.intern.kotam.aom.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * OpenAPI configuration class for Kotam project
 * Contains information about the project
 * It can be reached from the /swagger-ui.html endpoint
 */
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Kotam",
                        url = "https://github.com/i2i-Interns-2025/Kotam-AOM"
                ),
                description = "OpenAPI documentation for Kotam AOM project",
                title = "OpenAPI Specification - Kotam",
                version = "1.0"
        )
)
public class OpenApiConfiguration { }