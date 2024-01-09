package com.service.achievementservice.controller;

import com.service.achievementservice.payload.AchievementDetailsDTO;
import com.service.achievementservice.services.AchievementService;
import com.service.achievementservice.services.DbQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AchievementController {
    private final AchievementService achievementService;
    private final DbQueryService dbQueryService;

    public AchievementController(AchievementService achievementService, DbQueryService dbQueryService) {
        this.achievementService = achievementService;
        this.dbQueryService = dbQueryService;
    }

    @PostMapping("/users/{userId}/achievements/check-deck")
    public void checkDeckAchievements(@PathVariable Long userId) {
        achievementService.achievementCreationDeck(userId);
    }

    @PostMapping("/users/{userId}/achievements/check-session")
    public void checkSessionAchievements(@PathVariable Long userId) {
        achievementService.achievementSessionDaily(userId);
        achievementService.achievementStateSessions(userId);
    }

    @PostMapping("/users/{userId}/achievements/check-cards-learned")
    public void checkCardsLearnedAchievements(@PathVariable Long userId) {
        achievementService.achievementCardsLearnedDaily(userId);
        achievementService.achievementCardsLearned(userId);
    }

    @PostMapping("/users/{userId}/achievements/check-daily-login")
    public void checkDailyLoginAchievement(@PathVariable Long userId) {
        achievementService.achievementDailyLogin(userId);
    }

    @GetMapping("/achievements/{achievementId}")
    public ResponseEntity<AchievementDetailsDTO> getAchievementDetails(@RequestHeader Long userId, @PathVariable Long achievementId) {
        return dbQueryService.getAchievementDetails(achievementId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
