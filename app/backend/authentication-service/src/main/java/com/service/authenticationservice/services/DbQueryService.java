package com.service.authenticationservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.authenticationservice.payload.inc.UpdatePasswordDTO;
import com.service.authenticationservice.payload.inc.UserDTO;
import com.service.authenticationservice.payload.out.UpdatePasswordDTOUnauthorized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
    private final String dbApiBasePath;
    private final String userDbApiPath;
    private final String userEmailDbApiPath;
    private final Logger logger = LoggerFactory.getLogger(DbQueryService.class);

    public DbQueryService(ObjectMapper objectMapper, RestTemplate restTemplate, @Value("${db.api.path}") String dbPath) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.dbApiBasePath = dbPath;
        this.userDbApiPath = this.dbApiBasePath + "/users";
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

    public Optional<Long> getUserIdByEmail(String email) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(userEmailDbApiPath + "/" + email + "/id", String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.of(Long.valueOf(Objects.requireNonNull(responseEntity.getBody())))
                    : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Boolean> doesUserWithEmailExist(String email) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(userEmailDbApiPath + "/" + email + "/exists", String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.of(Boolean.parseBoolean(responseEntity.getBody()))
                    : Optional.empty();
        } catch (Exception e) {
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

    public Optional<Boolean> getVerificationStateUser(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(userDbApiPath + "/" + userId + "/verified", String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.of(Boolean.parseBoolean(responseEntity.getBody()))
                    : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean isTokenValid(String token) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(dbApiBasePath + "/user-token/" + token+ "/valid", String.class);
            return responseEntity.getStatusCode() == HttpStatus.NO_CONTENT;
        } catch (Exception e) {
            return false;
        }
    }

    public HttpStatusCode saveUser(UserDTO userDTO) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                userDbApiPath + "/",
                new HttpEntity<>(userDTO, headers),
                String.class);
        return responseEntity.getStatusCode();
    }

    public HttpStatusCode setVerificationStateToTrue(String token) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    dbApiBasePath + "/user-token/" + token,
                    HttpMethod.PUT,
                    new HttpEntity<>(headers),
                    Void.class
            );
            return responseEntity.getStatusCode();
        } catch (HttpClientErrorException e) { // Necessary if status-code 409 or 400 is returned.
            if (e.getStatusCode().equals(HttpStatus.CONFLICT)) {
                return HttpStatus.CONFLICT;
            } else if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                return HttpStatus.BAD_REQUEST;
            }
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public HttpStatusCode updateUserPassword(Long userId, UpdatePasswordDTO updatePasswordDTO) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    userDbApiPath + "/" + userId + "/password",
                    HttpMethod.PUT,
                    new HttpEntity<>(updatePasswordDTO, headers),
                    Void.class
            );
            return responseEntity.getStatusCode();
        } catch (HttpClientErrorException e) {
            return e.getStatusCode();
        }
    }

    public HttpStatusCode updateUserPasswordUnauthorized(Long userId, UpdatePasswordDTOUnauthorized updatePasswordDTOUnauthorized) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    userDbApiPath + "/" + userId + "/password/unauthorized",
                    HttpMethod.PUT,
                    new HttpEntity<>(updatePasswordDTOUnauthorized, headers),
                    Void.class
            );
            return responseEntity.getStatusCode();
        } catch (HttpClientErrorException e) {
            return e.getStatusCode();
        }
    }

}
