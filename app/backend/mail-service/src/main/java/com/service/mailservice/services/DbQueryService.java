package com.service.mailservice.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.mailservice.payload.inc.UserDailyReminderDTO;
import com.service.mailservice.payload.out.UserTokenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class DbQueryService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String DB_API_BASE_PATH;
    private final String USER_DB_API_PATH;
    private final String USER_TOKEN_DB_API_PATH;
    private final Logger logger = LoggerFactory.getLogger(DbQueryService.class);


    public DbQueryService(@Value("${db.api.path}") String dbPath, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.DB_API_BASE_PATH = dbPath;
        this.USER_DB_API_PATH = this.DB_API_BASE_PATH + "/users";
        this.USER_TOKEN_DB_API_PATH = this.DB_API_BASE_PATH + "/user-token";
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public HttpStatusCode saveUserToken(UserTokenDTO userTokenDTO) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                USER_TOKEN_DB_API_PATH + "/",
                new HttpEntity<>(userTokenDTO, headers),
                String.class);
        return responseEntity.getStatusCode();
    }

    public Optional<String> getUserEmailFromId(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity( USER_DB_API_PATH + "/" + userId + "/email", String.class);
            return responseEntity.getStatusCode() == HttpStatus.OK
                    ? Optional.ofNullable(responseEntity.getBody())
                    : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<List<UserDailyReminderDTO>> getAllEmailsForDailyLearn() {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(USER_DB_API_PATH + "/email", String.class);
            return responseEntity.getStatusCode() == HttpStatus.OK
                    ? Optional.of(objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {}))
                    : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
