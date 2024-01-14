package com.service.databaseservice.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SuppressWarnings("common-java:DuplicatedBlocks") // Both is necessary on multiple services
public class AppConfig {
    @Value("${springdoc.swagger-ui.servers}")
    private String springDefaultServerSwaggerDoc;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url(springDefaultServerSwaggerDoc).description("Default Server"));
    }
}
