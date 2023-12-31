package com.service.sessionservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.sessionservice.payload.RatingCardHandlerDTO;
import com.service.sessionservice.payload.StatusTypeDTO;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

@Service
public class DbQueryService {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final String DB_API_BASE_PATH;
    private final Logger logger = LoggerFactory.getLogger(DbQueryService.class);

    public DbQueryService(ObjectMapper objectMapper, RestTemplate restTemplate, @Value("${db.api.path}") String dbPath) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.DB_API_BASE_PATH = dbPath;
    }

    public Pair<HttpStatusCode, Long> saveLearnSession(Long userId, Long deckId) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    DB_API_BASE_PATH + "/users/" + userId + "/decks/" + deckId + "/learn-sessions",
                    new HttpEntity<>(headers),
                    String.class);
            return Pair.of(responseEntity.getStatusCode(), Long.valueOf(Objects.requireNonNull(responseEntity.getBody())));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.debug(e.getMessage());
            return Pair.of(e.getStatusCode(), null);
        }
    }
    public HttpStatusCode updateLearnSessionDifficulty(Long userId, Long learnSessionId, RatingCardHandlerDTO ratingCardHandlerDTO) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    DB_API_BASE_PATH + "/users/" + userId + "/learn-sessions/" + learnSessionId +  "/rating",
                    HttpMethod.PUT,
                    new HttpEntity<>(ratingCardHandlerDTO, headers),
                    Void.class
            );
            return responseEntity.getStatusCode();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.debug(e.getMessage());
            return e.getStatusCode();
        }
    }

    public HttpStatusCode updateLearnSessionStatus(Long userId, Long learnSessionId, StatusTypeDTO statusTypeDTO) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    DB_API_BASE_PATH + "/users/" + userId + "/learn-sessions/" + learnSessionId + "/status",
                    HttpMethod.PUT,
                    new HttpEntity<>(statusTypeDTO, headers),
                    Void.class
            );
            return responseEntity.getStatusCode();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.debug(e.getMessage());
            return e.getStatusCode();
        }
    }

    public Pair<HttpStatusCode, Object> getLongestUnseenCard(Long userId, Long deckId) {
        try {
            String url = DB_API_BASE_PATH + "/users/" + userId + "/decks/" + deckId + "/cards/longest-unseen";
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Pair.of(HttpStatus.OK, Optional.ofNullable(responseEntity.getBody()))
                    : Pair.of(responseEntity.getStatusCode(), Optional.empty());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.debug(e.getMessage());
            return Pair.of(e.getStatusCode(), Optional.empty());
        }
    }

    public Pair<HttpStatusCode, Long> savePeekSession(Long userId, Long deckId) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    DB_API_BASE_PATH + "/users/" + userId + "/decks/" + deckId + "/peek-sessions",
                    new HttpEntity<>(headers),
                    String.class);
            return Pair.of(responseEntity.getStatusCode(), Long.valueOf(Objects.requireNonNull(responseEntity.getBody())));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.debug(e.getMessage());
            return Pair.of(e.getStatusCode(), null);
        }
    }

    public HttpStatusCode savePeekSessionCard(Long userId, Long peekSessionId, Long cardId) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    DB_API_BASE_PATH + "/users/" + userId + "/peek-sessions/" + peekSessionId +  "/cards/" + cardId,
                    new HttpEntity<>(headers),
                    String.class);
            return responseEntity.getStatusCode();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.debug(e.getMessage());
            return e.getStatusCode();
        }
    }

    public HttpStatusCode updatePeekSessionStatus(Long userId, Long peekSessionId, StatusTypeDTO statusTypeDTO) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    DB_API_BASE_PATH + "/users/" + userId + "/peek-sessions/" + peekSessionId + "/status",
                    HttpMethod.PUT,
                    new HttpEntity<>(statusTypeDTO, headers),
                    Void.class
            );
            return responseEntity.getStatusCode();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.debug(e.getMessage());
            return e.getStatusCode();
        }
    }

    public Pair<HttpStatusCode, Object> getRandomPeekSessionCard(Long userId, Long peekSessionId) {
        try {
            String url = DB_API_BASE_PATH + "/users/" + userId + "/peek-sessions/" + peekSessionId + "/cards/random-card";
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Pair.of(HttpStatus.OK, Optional.ofNullable(responseEntity.getBody()))
                    : Pair.of(responseEntity.getStatusCode(), Optional.empty());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.debug(e.getMessage());
            return Pair.of(e.getStatusCode(), Optional.empty());
        }
    }
}
