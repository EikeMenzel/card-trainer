package com.service.databaseservice.payload.out;

public record AchievementDetailsDTO(Long achievementId, String achievementName, String description, Boolean daily, Long imageId) {
}
