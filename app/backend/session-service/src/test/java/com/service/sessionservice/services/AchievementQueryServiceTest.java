package com.service.sessionservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
class AchievementQueryServiceTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private AchievementQueryService achievementQueryService;

    @Test
    void whenCheckSessionAchievements_Success() {
        ResponseEntity<String> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.postForEntity(
                any(String.class), any(), eq(String.class))).thenReturn(mockResponse);

        achievementQueryService.checkSessionAchievements(1L);

        verify(restTemplate, times(1)).postForEntity(
                any(String.class), any(), eq(String.class));
    }

    @Test
    void whenCheckSessionAchievements_HandlesException() {
        when(restTemplate.postForEntity(
                any(String.class), any(), eq(String.class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        achievementQueryService.checkSessionAchievements(1L);

        verify(restTemplate, times(1)).postForEntity(
                any(String.class), any(), eq(String.class));
    }

    @Test
    void whenCheckCardsLearnedAchievements_Success() {
        ResponseEntity<String> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.postForEntity(
                any(String.class), any(), eq(String.class))).thenReturn(mockResponse);

        achievementQueryService.checkCardsLearnedAchievements(1L);

        verify(restTemplate, times(1)).postForEntity(
                any(String.class), any(), eq(String.class));
    }

    @Test
    void whenCheckCardsLearnedAchievements_HandlesException() {
        when(restTemplate.postForEntity(
                any(String.class), any(), eq(String.class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        achievementQueryService.checkCardsLearnedAchievements(1L);

        verify(restTemplate, times(1)).postForEntity(
                any(String.class), any(), eq(String.class));
    }
}
