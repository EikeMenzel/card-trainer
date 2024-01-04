package com.service.cardsservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AchievementQueryService {
    private final RestTemplate restTemplate;
    private final String achievementServiceUrl;
    public AchievementQueryService(RestTemplate restTemplate, @Value("${achievement-service.api.path}") String achievementServiceUrl) {
        this.restTemplate = restTemplate;
        this.achievementServiceUrl = achievementServiceUrl + "/api/v1";
    }

    public HttpStatusCode checkDeckAchievements(Long userId) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                achievementServiceUrl + "/users/" + userId + "/achievements/check-deck",
                new HttpEntity<>(headers),
                String.class);
        return responseEntity.getStatusCode();
    }

    public HttpStatusCode checkDailyLoginAchievement(Long userId) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                achievementServiceUrl + "/users/" + userId + "/achievements/check-daily-login",
                new HttpEntity<>(headers),
                String.class);
        return responseEntity.getStatusCode();
    }
}
