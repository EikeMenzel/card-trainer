package com.service.cardsservice.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@SuppressWarnings("common-java:DuplicatedBlocks") // Both is necessary on multiple services
public class AppConfig {
    @Value("${springdoc.swagger-ui.servers}")
    private String springDefaultServerSwaggerDoc;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url(springDefaultServerSwaggerDoc).description("Default Server"));
    }
}
