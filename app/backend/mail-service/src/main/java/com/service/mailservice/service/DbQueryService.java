package com.service.mailservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.mailservice.payload.out.UserTokenDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class DbQueryService {
    private final RestTemplate restTemplate;

    //@Value("database.api.path")
    private static final String GENERIC_DB_API_PATH = "http://localhost:8086/api/v1/db";
    private static final String USER_TOKEN_DB_API_PATH  =  GENERIC_DB_API_PATH + "/user-token";
    public DbQueryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(GENERIC_DB_API_PATH + "/users/" + userId + "/email", String.class);
            return responseEntity.getStatusCode() == HttpStatus.OK
                    ? Optional.ofNullable(responseEntity.getBody())
                    : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
