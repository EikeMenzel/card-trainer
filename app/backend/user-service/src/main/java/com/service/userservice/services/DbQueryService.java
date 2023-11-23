package com.service.userservice.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.userservice.payload.inc.UserAccountInformationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class DbQueryService {
    private final Logger logger = LoggerFactory.getLogger(DbQueryService.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String USER_DB_API_PATH;
    private final String ACHIEVEMENT_USER_DB_API_PATH;

    public DbQueryService(RestTemplate restTemplate, ObjectMapper objectMapper, @Value("${db.api.path}") String dbPath) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.USER_DB_API_PATH = dbPath + "/users";
        this.ACHIEVEMENT_USER_DB_API_PATH = dbPath + "/user-achievements";
    }

    public Optional<UserAccountInformationDTO> getAccountInformation(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(USER_DB_API_PATH + "/" + userId + "/account", String.class);

            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.of(objectMapper.readValue(responseEntity.getBody(), UserAccountInformationDTO.class))
                    : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Long> getAchievementIds(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(ACHIEVEMENT_USER_DB_API_PATH + "/users/" + userId + "/achievements/ids", String.class);
            if(responseEntity.getStatusCode() == HttpStatus.OK)
                return objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {});
        } catch (Exception e) {
            return List.of();
        }
        return List.of();
    }

    public Optional<UserAccountInformationDTO> updateAccountInformation(Long userId, UserAccountInformationDTO userAccountInformationDTO) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UserAccountInformationDTO> requestEntity = new HttpEntity<>(userAccountInformationDTO, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    USER_DB_API_PATH + "/" + userId + "/account",
                    HttpMethod.PUT,
                    requestEntity,
                    String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return Optional.of(objectMapper.readValue(responseEntity.getBody(), UserAccountInformationDTO.class));
            }
        } catch (HttpClientErrorException e) {
            logger.error("HttpClientErrorException: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Exception occurred: " + e.getMessage(), e);
        }
        return Optional.empty();
    }
}
