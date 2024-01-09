package com.service.databaseservice.controller;

import com.service.databaseservice.payload.out.AchievementDetailsDTO;
import com.service.databaseservice.services.UserAchievementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/db")
public class AchievementController {
    private final UserAchievementService userAchievementService;

    public AchievementController(UserAchievementService userAchievementService) {
        this.userAchievementService = userAchievementService;
    }

    @GetMapping("/achievements/{achievementId}")
    public ResponseEntity<AchievementDetailsDTO> getAchievementDetails(@PathVariable Long achievementId) {
        return userAchievementService.getAchievementDetails(achievementId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
