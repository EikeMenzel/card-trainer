package com.service.authenticationservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.authenticationservice.payload.inc.RainbowListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class AppConfig {
    private final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private final ObjectMapper objectMapper;

    //@Value("database.api.path")
    private String dbApiPath = "http://localhost:8086/api/v1/db";

    public AppConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RainbowListDTO queryRainbowTable() {
        try {
            RainbowListDTO rainbowListDTO =  objectMapper.readValue(
                    restTemplate()
                            .getForEntity(dbApiPath + "/rainbows", String.class)
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
