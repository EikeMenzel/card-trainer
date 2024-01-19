package com.service.cardsservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
class AchievementQueryServiceTest {
    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private AchievementQueryService achievementQueryService;
    private final Logger logger = LoggerFactory.getLogger(AchievementQueryService.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void whenCheckDeckAchievements_Success() {
        ResponseEntity<String> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        Long userId = 1L;

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(String.class))).thenReturn(mockResponse);

        achievementQueryService.checkDeckAchievements(userId);

        verify(restTemplate, times(1)).postForEntity(
                any(String.class), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void whenCheckDailyLoginAchievement_Success() {
        ResponseEntity<String> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        Long userId = 1L;

        when(restTemplate.postForEntity(
                any(String.class), any(HttpEntity.class), eq(String.class))).thenReturn(mockResponse);

        achievementQueryService.checkDailyLoginAchievement(userId);

        verify(restTemplate, times(1)).postForEntity(
                any(String.class), any(HttpEntity.class), eq(String.class));
    }
}
