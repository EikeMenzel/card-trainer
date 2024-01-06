package com.service.userservice.payload.inc;

import java.util.List;

public class UserAccountInformationAchievementsDTO extends UserAccountInformationDTO {
    private final List<Long> achievementIds;
    public UserAccountInformationAchievementsDTO(String username, String email, Integer cardsToLearn, Boolean receiveLearnNotification, String langCode, Integer loginStreak, List<Long> achievementIds) {
        super(username, email, cardsToLearn, receiveLearnNotification, langCode, loginStreak);
        this.achievementIds = achievementIds;
    }

    public List<Long> getAchievementIds() {
        return achievementIds;
    }
}
