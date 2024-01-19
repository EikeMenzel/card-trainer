package com.service.achievementservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.achievementservice.payload.AchievementDetailsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DbQueryServiceTest {
    private DbQueryService dbQueryService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dbQueryService = new DbQueryService(restTemplate, objectMapper, "db/api/path", "users/path");
    }

    @Test
    void whenGetDeckCount_Failure_() {
        Long userId = 1L;
        String url = "db/api/path/users/path/1/decks/count";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("3", HttpStatus.OK);

        when(restTemplate.getForEntity(url, String.class)).thenReturn(responseEntity);

        Optional<Integer> result = dbQueryService.getDeckCount(userId);

        assertFalse(result.isPresent());
    }

    @Test
    void whenGetDeckCount_Failure() {
        Long userId = 1L;
        String url = "db/api/path/users/path/1/decks/count";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);

        when(restTemplate.getForEntity(url, String.class)).thenReturn(responseEntity);

        Optional<Integer> result = dbQueryService.getDeckCount(userId);

        assertFalse(result.isPresent());
    }

    @Test
    void whenGetLearnSessionCount_Failure() {
        Long userId = 1L;
        String url = "db/api/path/users/path/1/learn-sessions/count";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("5", HttpStatus.OK);

        when(restTemplate.getForEntity(url, String.class)).thenReturn(responseEntity);

        Optional<Integer> result = dbQueryService.getLearnSessionCount(userId);

        assertFalse(result.isPresent());
    }

    @Test
    void whenGetCardsLearnedCount_Failure() {
        Long userId = 1L;
        String url = "db/api/path/users/path/1/cards-learned";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("100", HttpStatus.OK);

        when(restTemplate.getForEntity(url, String.class)).thenReturn(responseEntity);

        Optional<Integer> result = dbQueryService.getCardsLearnedCount(userId);

        assertFalse(result.isPresent());
    }

    @Test
    void whenDoesUserHaveAchievement_Failure() {
        Long userId = 1L;
        Long achievementId = 1L;
        String url = "db/api/path/user-achievements/users/path/1/achievements/1/exists";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("true", HttpStatus.OK);

        when(restTemplate.getForEntity(url, String.class)).thenReturn(responseEntity);

        Optional<Boolean> result = dbQueryService.doesUserHaveAchievement(userId, achievementId);

        assertFalse(result.isPresent());
    }

    @Test
    void whenGetCardsLearnedDailyCount_Failure() {
        Long userId = 1L;
        String url = "db/api/path/users/path/1/learn-sessions/cards-learned/daily";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("10", HttpStatus.OK);

        when(restTemplate.getForEntity(url, String.class)).thenReturn(responseEntity);

        Optional<Integer> result = dbQueryService.getCardsLearnedDailyCount(userId);

        assertFalse(result.isPresent());
    }

    @Test
    void whenDoesUserCompletedLearnSessionToday_Failure() {
        Long userId = 1L;
        String url = "db/api/path/users/path/1/learn-sessions/daily";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("true", HttpStatus.OK);

        when(restTemplate.getForEntity(url, String.class)).thenReturn(responseEntity);

        Optional<Boolean> result = dbQueryService.doesUserCompletedLearnSessionToday(userId);

        assertFalse(result.isPresent());
    }

    @Test
    void whenDidUserLoginToday_Failure() {
        Long userId = 1L;
        String url = "db/api/path/users/path/1/user-login-tracker";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("true", HttpStatus.OK);

        when(restTemplate.getForEntity(url, String.class)).thenReturn(responseEntity);

        Optional<Boolean> result = dbQueryService.didUserLoginToday(userId);

        assertFalse(result.isPresent());
    }

    @Test
    void whenGetAchievementDetails_Failure() throws JsonProcessingException {
        Long achievementId = 1L;
        String url = "db/api/path/achievements/1";
        AchievementDetailsDTO achievementDetailsDTO = new AchievementDetailsDTO(1L, "X", "Y", false, 1L);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(objectMapper.writeValueAsString(achievementDetailsDTO), HttpStatus.OK);

        when(restTemplate.getForEntity(url, String.class)).thenReturn(responseEntity);
        when(objectMapper.readValue(responseEntity.getBody(), new TypeReference<Optional<AchievementDetailsDTO>>() {})).thenReturn(Optional.of(achievementDetailsDTO));

        Optional<AchievementDetailsDTO> result = dbQueryService.getAchievementDetails(achievementId);

        assertNull(result);
    }
}
