package com.service.achievementservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AchievementService {
    private final DbQueryService dbQueryService;
    private final WebSocketService webSocketService;
    private final Logger logger = LoggerFactory.getLogger(AchievementService.class);

    public AchievementService(DbQueryService dbQueryService, WebSocketService webSocketService) {
        this.dbQueryService = dbQueryService;
        this.webSocketService = webSocketService;
    }

    private void giveAchievementIfNotPresent(Long userId, Long achievementId) {
        if (dbQueryService.doesUserHaveAchievement(userId, achievementId).isPresent())
            return; // Achievement Notification was already sent before

        if (dbQueryService.saveUserAchievement(userId, achievementId) == HttpStatus.CREATED)
            webSocketService.sendAchievementNotification(userId, achievementId);
    }

    public void achievementCreationDeck(Long userId) {
        Optional<Integer> deckCountOptional = dbQueryService.getDeckCount(userId);
        if (deckCountOptional.isPresent() && deckCountOptional.get() > 0)
            giveAchievementIfNotPresent(userId, 1L);
    }

    private void handleSingleSession(Long userId) {
        giveAchievementIfNotPresent(userId, 2L);
    }

    private void handleFiftySessions(Long userId) {
        giveAchievementIfNotPresent(userId, 3L);
    }

    private void handleHundredSessions(Long userId) {
        giveAchievementIfNotPresent(userId, 4L);
    }

    private void handleFiveHundredSessions(Long userId) {
        giveAchievementIfNotPresent(userId, 5L);
    }

    private void handleHundredCardsLearned(Long userId) {
        giveAchievementIfNotPresent(userId, 6L);
    }
    private void handleFiveHundredCardsLearned(Long userId) {
        giveAchievementIfNotPresent(userId, 7L);
    }
    private void handleTwentyFiveHundredCardsLearned(Long userId) {
        giveAchievementIfNotPresent(userId, 8L);
    }

    public void achievementStateSessions(Long userId) {
        Optional<Integer> sessionsCountOptional = dbQueryService.getLearnSessionCount(userId);
        if (sessionsCountOptional.isEmpty())
            return;

        switch (sessionsCountOptional.get()) {
            case 1 -> handleSingleSession(userId);
            case 50 -> handleFiftySessions(userId);
            case 100 -> handleHundredSessions(userId);
            case 500 -> handleFiveHundredSessions(userId);
        }
    }

    public void achievementCardsLearned(Long userId) {
        Optional<Integer> cardsLearnedCountOptional = dbQueryService.getCardsLearnedCount(userId);
        if (cardsLearnedCountOptional.isEmpty())
            return;

        switch (cardsLearnedCountOptional.get()) {
            case 100 -> handleHundredCardsLearned(userId);
            case 500 -> handleFiveHundredCardsLearned(userId);
            case 2500 -> handleTwentyFiveHundredCardsLearned(userId);
        }
    }

    public void achievementSessionDaily(Long userId) {
        Optional<Boolean> booleanOptional = dbQueryService.doesUserCompletedLearnSessionToday(userId);
        if(booleanOptional.isPresent() && booleanOptional.get())
            giveAchievementIfNotPresent(userId, 10L);
    }

    public void achievementCardsLearnedDaily(Long userId) {
        Optional<Integer> cardsLearnedDailyOptional = dbQueryService.getCardsLearnedDailyCount(userId);
        if(cardsLearnedDailyOptional.isEmpty())
            return;

        switch (cardsLearnedDailyOptional.get()) {
            case 10 -> giveAchievementIfNotPresent(userId, 11L);
            case 50 -> giveAchievementIfNotPresent(userId, 12L);
        }
    }

    public void achievementDailyLogin(Long userId) {
        Optional<Boolean> dailyLogin = dbQueryService.didUserLoginToday(userId);

        if(dailyLogin.isPresent() && dailyLogin.get())
            giveAchievementIfNotPresent(userId, 9L);
    }
}
