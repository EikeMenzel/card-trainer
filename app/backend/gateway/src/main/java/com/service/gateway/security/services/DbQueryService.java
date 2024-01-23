package com.service.gateway.security.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.gateway.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class DbQueryService {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final String userDbApiPath;
    private final String userEmailDbApiPath;
    private final Logger logger = LoggerFactory.getLogger(DbQueryService.class);

    public DbQueryService(ObjectMapper objectMapper, RestTemplate restTemplate, @Value("${db.api.path}") String dbPath) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        String dbApiBasePath = dbPath + "/api/v1/db";
        this.userDbApiPath = dbApiBasePath + "/users";
        this.userEmailDbApiPath = this.userDbApiPath + "/emails";
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(userEmailDbApiPath + "/" + email, String.class);

            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.of(objectMapper.readValue(responseEntity.getBody(), UserDTO.class))
                    : Optional.empty();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<String> getUserEmailFromId(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(userDbApiPath + "/" + userId + "/email", String.class);
            return responseEntity.getStatusCode() == HttpStatus.OK
                    ? Optional.ofNullable(responseEntity.getBody())
                    : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
