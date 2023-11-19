package com.service.authenticationservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.authenticationservice.payload.inc.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

@Service
public class DbQueryService {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    //@Value("database.api.path")
    private static final String GENERIC_DB_API_PATH = "http://localhost:8086/api/v1/db";
    private static final String USER_DB_API_PATH =  GENERIC_DB_API_PATH + "/users";
    private final Logger logger =  LoggerFactory.getLogger(DbQueryService.class);

    public DbQueryService(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(USER_DB_API_PATH + "/email/" + email, String.class);

            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.of(objectMapper.readValue(responseEntity.getBody(), UserDTO.class))
                    : Optional.empty();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Long> getUserIdByEmail(String email) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(USER_DB_API_PATH + "/email/" + email + "/id", String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.of(Long.valueOf(Objects.requireNonNull(responseEntity.getBody())))
                    : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Boolean> doesUserWithEmailExist(String email) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(USER_DB_API_PATH + "/email/" + email + "/exists", String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.of(Boolean.parseBoolean(responseEntity.getBody()))
                    : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
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

    public Optional<Boolean> getVerificationStateUser(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(USER_DB_API_PATH + "/"+ userId +  "/verified", String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.of(Boolean.parseBoolean(responseEntity.getBody()))
                    : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public HttpStatusCode saveUser(UserDTO userDTO) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                USER_DB_API_PATH + "/",
                new HttpEntity<>(userDTO, headers),
                String.class);
        return responseEntity.getStatusCode();
    }

    public HttpStatusCode updateUserWithToken(String token) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    GENERIC_DB_API_PATH + "/user-token/" + token,
                    HttpMethod.PUT,
                    new HttpEntity<>(headers),
                    Void.class
            );
            return responseEntity.getStatusCode();
        } catch (HttpClientErrorException e) { // Necessary if status-code 409 or 400 is returned.
            if (e.getStatusCode().equals(HttpStatus.CONFLICT)) {
                return HttpStatus.CONFLICT;
            } else if(e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                return HttpStatus.BAD_REQUEST;
            }
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
