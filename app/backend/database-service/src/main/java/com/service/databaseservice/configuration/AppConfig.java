package com.service.databaseservice.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${springdoc.swagger-ui.servers}")
    private String SPRING_DEFAULT_SERVER_SWAGGER_DOC;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url(SPRING_DEFAULT_SERVER_SWAGGER_DOC).description("Default Server"));
    }
}
