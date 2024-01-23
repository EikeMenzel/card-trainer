package com.service.cardsservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AchievementQueryService {
    private final Logger logger = LoggerFactory.getLogger(AchievementQueryService.class);
    private final RestTemplate restTemplate;
    private final String achievementServiceUrl;
    public AchievementQueryService(RestTemplate restTemplate, @Value("${achievement-service.api.path}") String achievementServiceUrl) {
        this.restTemplate = restTemplate;
        this.achievementServiceUrl = achievementServiceUrl + "/api/v1";
    }

    public void checkDeckAchievements(Long userId) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            restTemplate.postForEntity(
                    achievementServiceUrl + "/users/" + userId + "/achievements/check-deck",
                    new HttpEntity<>(headers),
                    String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.debug(e.getMessage());
        }
    }

    public void checkDailyLoginAchievement(Long userId) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            restTemplate.postForEntity(
                    achievementServiceUrl + "/users/" + userId + "/achievements/check-daily-login",
                    new HttpEntity<>(headers),
                    String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.debug(e.getMessage());
        }
    }
}
