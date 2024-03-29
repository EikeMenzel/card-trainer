package com.service.achievementservice.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.achievementservice.payload.AchievementDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

@Service
public class DbQueryService {
    private final Logger logger = LoggerFactory.getLogger(DbQueryService.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String dbPath;
    private final String usersPath;
    public DbQueryService(RestTemplate restTemplate, ObjectMapper objectMapper, @Value("${db.api.path}") String dbPath, @Value("${users.path}") String usersPath) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.dbPath = dbPath;
        this.usersPath = usersPath;
    }

    public Optional<Integer> getDeckCount(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(dbPath + usersPath + userId + "/decks/count", String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Integer.valueOf(Objects.requireNonNull(responseEntity.getBody())).describeConstable()
                    : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Integer> getLearnSessionCount(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(dbPath + usersPath + userId + "/learn-sessions/count", String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Integer.valueOf(Objects.requireNonNull(responseEntity.getBody())).describeConstable()
                    : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Integer> getCardsLearnedCount(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(dbPath + usersPath + userId + "/cards-learned", String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Integer.valueOf(Objects.requireNonNull(responseEntity.getBody())).describeConstable()
                    : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }
    public Optional<Boolean> doesUserHaveAchievement(Long userId, Long achievementId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(dbPath + "/user-achievements" + usersPath + userId + "/achievements/" + achievementId + "/exists", String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.of(Boolean.parseBoolean(responseEntity.getBody()))
                    : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Integer> getCardsLearnedDailyCount(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(dbPath + usersPath + userId + "/learn-sessions/cards-learned/daily", String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Integer.valueOf(Objects.requireNonNull(responseEntity.getBody())).describeConstable()
                    : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Boolean> doesUserCompletedLearnSessionToday(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(dbPath + usersPath + userId + "/learn-sessions/daily", String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.of(Boolean.parseBoolean(responseEntity.getBody()))
                    : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public HttpStatusCode saveUserAchievement(Long userId, Long achievementId) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                dbPath + "/user-achievements"  + usersPath + userId + "/achievements",
                new HttpEntity<>(achievementId, headers),
                String.class);
        return responseEntity.getStatusCode();
    }

    public Optional<Boolean> didUserLoginToday(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(dbPath + usersPath + userId + "/user-login-tracker", String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.of(Boolean.parseBoolean(responseEntity.getBody()))
                    : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<AchievementDetailsDTO> getAchievementDetails(Long achievementId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(dbPath + "/achievements/" + achievementId, String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? objectMapper.readValue(responseEntity.getBody(), new TypeReference<Optional<AchievementDetailsDTO>>() {})
                    : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }
}
