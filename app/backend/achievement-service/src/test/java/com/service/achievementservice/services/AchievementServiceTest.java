package com.service.achievementservice.services;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AchievementServiceTest {

    private final DbQueryService dbQueryService = mock(DbQueryService.class);
    private final WebSocketService webSocketService = mock(WebSocketService.class);
    private final AchievementService achievementService = new AchievementService(dbQueryService, webSocketService);

    @Test
    void achievementCreationDeck_WithDeckCountGreaterThanZero_GivesAchievement() {
        when(dbQueryService.getDeckCount(any())).thenReturn(Optional.of(1));
        when(dbQueryService.doesUserHaveAchievement(any(), any())).thenReturn(Optional.empty());
        when(dbQueryService.saveUserAchievement(any(), any())).thenReturn(HttpStatus.CREATED);

        achievementService.achievementCreationDeck(1L);

        verify(dbQueryService, times(1)).getDeckCount(1L);
        verify(dbQueryService, times(1)).doesUserHaveAchievement(1L, 1L);
        verify(dbQueryService, times(1)).saveUserAchievement(1L, 1L);
        verify(webSocketService, times(1)).sendAchievementNotification(1L, 1L);
    }

    @Test
    void achievementStateSessions_WithSessionCountEqualToOne_GivesAchievement() {
        when(dbQueryService.getLearnSessionCount(any())).thenReturn(Optional.of(1));
        when(dbQueryService.doesUserHaveAchievement(any(), any())).thenReturn(Optional.empty());
        when(dbQueryService.saveUserAchievement(any(), any())).thenReturn(HttpStatus.CREATED);

        achievementService.achievementStateSessions(1L);

        verify(dbQueryService, times(1)).getLearnSessionCount(1L);
        verify(dbQueryService, times(1)).doesUserHaveAchievement(1L, 2L);
        verify(dbQueryService, times(1)).saveUserAchievement(1L, 2L);
        verify(webSocketService, times(1)).sendAchievementNotification(1L, 2L);
    }

    @Test
    void achievementCardsLearned_WithCardsLearnedCountEqualToHundred_GivesAchievement() {
        when(dbQueryService.getCardsLearnedCount(any())).thenReturn(Optional.of(100));
        when(dbQueryService.doesUserHaveAchievement(any(), any())).thenReturn(Optional.empty());
        when(dbQueryService.saveUserAchievement(any(), any())).thenReturn(HttpStatus.CREATED);

        achievementService.achievementCardsLearned(1L);

        verify(dbQueryService, times(1)).getCardsLearnedCount(1L);
        verify(dbQueryService, times(1)).doesUserHaveAchievement(1L, 6L);
        verify(dbQueryService, times(1)).saveUserAchievement(1L, 6L);
        verify(webSocketService, times(1)).sendAchievementNotification(1L, 6L);
    }

    @Test
    void achievementSessionDaily_WithUserCompletedLearnSessionToday_GivesAchievement() {
        when(dbQueryService.doesUserCompletedLearnSessionToday(any())).thenReturn(Optional.of(true));
        when(dbQueryService.doesUserHaveAchievement(any(), any())).thenReturn(Optional.empty());
        when(dbQueryService.saveUserAchievement(any(), any())).thenReturn(HttpStatus.CREATED);

        achievementService.achievementSessionDaily(1L);

        verify(dbQueryService, times(1)).doesUserCompletedLearnSessionToday(1L);
        verify(dbQueryService, times(1)).doesUserHaveAchievement(1L, 10L);
        verify(dbQueryService, times(1)).saveUserAchievement(1L, 10L);
        verify(webSocketService, times(1)).sendAchievementNotification(1L, 10L);
    }

    @Test
    void achievementCardsLearnedDaily_WithCardsLearnedDailyCountEqualToTen_GivesAchievement() {
        when(dbQueryService.getCardsLearnedDailyCount(any())).thenReturn(Optional.of(10));
        when(dbQueryService.doesUserHaveAchievement(any(), any())).thenReturn(Optional.empty());
        when(dbQueryService.saveUserAchievement(any(), any())).thenReturn(HttpStatus.CREATED);

        achievementService.achievementCardsLearnedDaily(1L);

        verify(dbQueryService, times(1)).getCardsLearnedDailyCount(1L);
        verify(dbQueryService, times(1)).doesUserHaveAchievement(1L, 11L);
        verify(dbQueryService, times(1)).saveUserAchievement(1L, 11L);
        verify(webSocketService, times(1)).sendAchievementNotification(1L, 11L);
    }

    @Test
    void achievementDailyLogin_WithUserLoggedInToday_GivesAchievement() {
        when(dbQueryService.didUserLoginToday(any())).thenReturn(Optional.of(true));
        when(dbQueryService.doesUserHaveAchievement(any(), any())).thenReturn(Optional.empty());
        when(dbQueryService.saveUserAchievement(any(), any())).thenReturn(HttpStatus.CREATED);

        achievementService.achievementDailyLogin(1L);

        verify(dbQueryService, times(1)).didUserLoginToday(1L);
        verify(dbQueryService, times(1)).doesUserHaveAchievement(1L, 9L);
        verify(dbQueryService, times(1)).saveUserAchievement(1L, 9L);
        verify(webSocketService, times(1)).sendAchievementNotification(1L, 9L);
    }

    @Test
    void achievementSessionDaily_WithUserCompletedSessionToday_GivesAchievement() {
        when(dbQueryService.doesUserCompletedLearnSessionToday(any())).thenReturn(Optional.of(true));
        when(dbQueryService.doesUserHaveAchievement(any(), any())).thenReturn(Optional.empty());
        when(dbQueryService.saveUserAchievement(any(), any())).thenReturn(HttpStatus.CREATED);

        achievementService.achievementSessionDaily(1L);

        verify(dbQueryService, times(1)).doesUserCompletedLearnSessionToday(1L);
        verify(dbQueryService, times(1)).doesUserHaveAchievement(1L, 10L);
        verify(dbQueryService, times(1)).saveUserAchievement(1L, 10L);
        verify(webSocketService, times(1)).sendAchievementNotification(1L, 10L);
    }
}
