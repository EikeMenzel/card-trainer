package com.service.databaseservice.repository.achievements;

import com.service.databaseservice.model.achievements.UserLoginTracker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginTrackerRepository extends JpaRepository<UserLoginTracker, Long> {
}
