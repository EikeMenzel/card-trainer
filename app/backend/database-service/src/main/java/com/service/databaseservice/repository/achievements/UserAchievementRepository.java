package com.service.databaseservice.repository.achievements;

import com.service.databaseservice.model.achievements.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    List<UserAchievement> getUserAchievementByUser_Id(Long userId);
}
