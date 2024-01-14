package com.service.authenticationservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.authenticationservice.payload.inc.RainbowListDTO;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
@SuppressWarnings("common-java:DuplicatedBlocks") // Both is necessary on multiple services
public class AppConfig {
    private final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private final ObjectMapper objectMapper;

    @Value("${db.api.path}")
    private String dbApiBasePath;

    @Value("${springdoc.swagger-ui.servers}")
    private String springDefaultServerSwaggerDoc;

    public AppConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url(springDefaultServerSwaggerDoc).description("Default Server"));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RainbowListDTO queryRainbowTable() {
        try {
            var rainbowListDTO =  objectMapper.readValue(
                    restTemplate()
                            .getForEntity(dbApiBasePath + "/rainbows", String.class)
                            .getBody(),
                    RainbowListDTO.class
            );
            logger.info("Loaded: Rainbow-table");
            return rainbowListDTO;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new RainbowListDTO(Collections.emptySet());
        }
    }
}
