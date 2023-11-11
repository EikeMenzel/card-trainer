package com.service.databaseservice.repository.achievements;

import com.service.databaseservice.model.achievements.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
}
