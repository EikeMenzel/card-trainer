package com.service.databaseservice.controller;

import com.service.databaseservice.services.UserAchievementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/db/user-achievements")
public class UserAchievementsController {
    private final UserAchievementService userAchievementService;

    public UserAchievementsController(UserAchievementService userAchievementService) {
        this.userAchievementService = userAchievementService;
    }

    @GetMapping("/users/{userId}/achievements/ids")
    public ResponseEntity<List<Long>> getAllAchievementsAsIdFromUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userAchievementService.getAllUserAchievementIdsByUserId(userId));
    }

    @GetMapping("/users/{userId}/achievements/{achievementId}/exists")
    public ResponseEntity<Void> doesUserHaveAchievement(@PathVariable Long userId, @PathVariable Long achievementId) {
        return userAchievementService.doesUserHaveAchievement(userId, achievementId)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/users/{userId}/achievements")
    public ResponseEntity<Void> saveUserAchievement(@PathVariable Long userId, @RequestBody Long achievementId) {
        return userAchievementService.saveUserAchievement(userId, achievementId)
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.internalServerError().build();
    }
}
