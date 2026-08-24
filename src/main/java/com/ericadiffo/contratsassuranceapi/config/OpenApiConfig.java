package com.ericadiffo.contratsassuranceapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig{
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gestion de Contrats d'Assurance")
                        .description("API REST pour la gestion des clients, contrats et garanties d'assurance")
                        .version("1.0.0"));
    }
}