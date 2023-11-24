package com.service.databaseservice.controller;

import com.service.databaseservice.services.UserAchievementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
