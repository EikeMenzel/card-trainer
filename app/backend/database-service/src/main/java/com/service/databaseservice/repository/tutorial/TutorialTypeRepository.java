package com.service.databaseservice.repository.tutorial;

import com.service.databaseservice.model.tutorial.TutorialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorialTypeRepository extends JpaRepository<TutorialType, Long> {
    Optional<TutorialType> findTutorialTypeByType(String type);
}
