package com.service.achievementservice.payload;

public record AchievementDetailsDTO(Long achievementId, String achievementName, String description, Boolean daily, Long imageId) {
}
