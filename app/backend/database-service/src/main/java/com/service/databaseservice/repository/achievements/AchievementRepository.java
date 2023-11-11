package com.service.databaseservice.repository.achievements;

import com.service.databaseservice.model.achievements.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
}
