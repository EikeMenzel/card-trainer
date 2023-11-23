package com.service.userservice.payload.inc;

import java.util.List;

public class UserAccountInformationAchievementsDTO extends UserAccountInformationDTO {
    private final List<Long> achievementIds;
    public UserAccountInformationAchievementsDTO(String username, String email, Integer cardsToLearn, Boolean receiveLearnNotification, String langCode, List<Long> achievementIds) {
        super(username, email, cardsToLearn, receiveLearnNotification, langCode);
        this.achievementIds = achievementIds;
    }

    public List<Long> getAchievementIds() {
        return achievementIds;
    }
}
