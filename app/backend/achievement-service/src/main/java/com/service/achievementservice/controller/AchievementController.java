package com.service.achievementservice.controller;

import com.service.achievementservice.services.AchievementService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class AchievementController {
    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @PostMapping("/{userId}/achievements/check-deck")
    public void checkDeckAchievements(@PathVariable Long userId) {
        achievementService.achievementCreationDeck(userId);
    }

    @PostMapping("/{userId}/achievements/check-session")
    public void checkSessionAchievements(@PathVariable Long userId) {
        achievementService.achievementSessionDaily(userId);
        achievementService.achievementStateSessions(userId);
    }

    @PostMapping("/{userId}/achievements/check-cards-learned")
    public void checkCardsLearnedAchievements(@PathVariable Long userId) {
        achievementService.achievementCardsLearnedDaily(userId);
        achievementService.achievementCardsLearned(userId);
    }

    @PostMapping("/{userId}/achievements/check-daily-login")
    public void checkDailyLoginAchievement(@PathVariable Long userId) {
        achievementService.achievementDailyLogin(userId);
    }
}
