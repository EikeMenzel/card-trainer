package com.service.databaseservice.repository.tutorial;

import com.service.databaseservice.model.tutorial.UserTutorialPage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTutorialPageRepository extends JpaRepository<UserTutorialPage, Long> {
    Boolean existsUserTutorialPageByUserIdAndTutorialTypeType(Long userId, String type);
}
