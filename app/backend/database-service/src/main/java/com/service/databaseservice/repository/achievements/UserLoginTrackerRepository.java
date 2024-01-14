package com.service.databaseservice.repository.achievements;

import com.service.databaseservice.model.achievements.UserLoginTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserLoginTrackerRepository extends JpaRepository<UserLoginTracker, Long> {
    @Query("SELECT CASE WHEN COUNT(ult) > 0 THEN TRUE ELSE FALSE END FROM UserLoginTracker ult WHERE ult.user.id = :userId AND DATE(ult.date) = CURRENT_DATE")
    Boolean existsByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT CAST(ult.date AS DATE) AS login_date FROM UserLoginTracker ult WHERE ult.user.id = :userId ORDER BY login_date DESC")
    List<Date> findDistinctLoginDatesByUserId(@Param("userId") Long userId);

}
