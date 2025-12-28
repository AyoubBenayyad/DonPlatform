package com.plateforme.dons.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI donationPlatformOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API Plateforme de Dons")
                        .description("Documentation de l'API REST pour la plateforme de dons d'objets")
                        .version("v1.0"));
    }
}
