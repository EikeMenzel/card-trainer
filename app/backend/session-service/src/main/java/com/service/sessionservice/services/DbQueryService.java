package com.service.sessionservice.services;

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
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(DbQueryService.class);
    private final String usersDbApiPath;
    private final String decksPath;
    private final String peekSessionPath;
    private final String learnSessionPath;

    public DbQueryService(RestTemplate restTemplate, @Value("${db.api.path}") String dbPath, @Value("${decks.path}") String decksPath, @Value("${learnsession.path}") String learnSessionPath, @Value("${peeksession.path}") String peekSessionPath) {
        this.restTemplate = restTemplate;
        this.usersDbApiPath = dbPath + "/users";
        this.decksPath = decksPath;
        this.peekSessionPath = peekSessionPath;
        this.learnSessionPath = learnSessionPath;
    }

    public Pair<HttpStatusCode, Long> saveLearnSession(Long userId, Long deckId) {
        return getHttpStatusCodeLongPair(userId, deckId, learnSessionPath);
    }

    private Pair<HttpStatusCode, Long> getHttpStatusCodeLongPair(Long userId, Long deckId, String learnSessionPath) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    usersDbApiPath + "/" + userId + decksPath + deckId + learnSessionPath,
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
                    usersDbApiPath + "/" + userId + learnSessionPath + "/" + learnSessionId +  "/rating",
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
        return getHttpStatusCode(userId, learnSessionId, statusTypeDTO, learnSessionPath);
    }

    private HttpStatusCode getHttpStatusCode(Long userId, Long learnSessionId, StatusTypeDTO statusTypeDTO, String learnSessionPath) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    usersDbApiPath + "/" + userId + learnSessionPath + "/" + learnSessionId + "/status",
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
            String url = usersDbApiPath + "/" + userId + decksPath + deckId + "/cards/longest-unseen";
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
        return getHttpStatusCodeLongPair(userId, deckId, peekSessionPath);
    }

    public HttpStatusCode savePeekSessionCard(Long userId, Long peekSessionId, Long cardId) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    usersDbApiPath + "/" + userId + peekSessionPath + "/" + peekSessionId +  "/cards/" + cardId,
                    new HttpEntity<>(headers),
                    String.class);
            return responseEntity.getStatusCode();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.debug(e.getMessage());
            return e.getStatusCode();
        }
    }

    public HttpStatusCode updatePeekSessionStatus(Long userId, Long peekSessionId, StatusTypeDTO statusTypeDTO) {
        return getHttpStatusCode(userId, peekSessionId, statusTypeDTO, peekSessionPath);
    }

    public Pair<HttpStatusCode, Object> getRandomPeekSessionCard(Long userId, Long peekSessionId) {
        try {
            String url = usersDbApiPath + "/" + userId + peekSessionPath + "/" + peekSessionId + "/cards/random-card";
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
