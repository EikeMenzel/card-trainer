package com.service.databaseservice.repository;

import com.service.databaseservice.model.Repetition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepetitionRepository extends JpaRepository<Repetition, Long> {
    Optional<Repetition> getRepetitionByCard_Id(Long cardId);
}
