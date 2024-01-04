package com.service.databaseservice.repository.achievements;

import com.service.databaseservice.model.achievements.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    List<UserAchievement> getUserAchievementsByUser_Id(Long userId);
    @Query("SELECT CASE WHEN COUNT(ua) > 0 THEN TRUE ELSE FALSE END " +
            "FROM UserAchievement ua WHERE ua.achievement.id = :achievementId " +
            "AND ua.user.id = :userId AND (ua.achievement.isDaily = FALSE " +
            "OR (ua.achievement.isDaily = TRUE AND DATE(ua.achievedAt) = CURRENT_DATE AND ua.id = (SELECT MAX(ua2.id) FROM UserAchievement ua2 WHERE ua2.achievement.id = :achievementId AND ua2.user.id = :userId AND ua2.achievement.isDaily = TRUE AND DATE(ua2.achievedAt) = CURRENT_DATE)))")
    Boolean hasUserAchieved(Long achievementId, Long userId);
}
