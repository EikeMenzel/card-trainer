package com.service.databaseservice.repository;

import com.service.databaseservice.model.Repetition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepetitionRepository extends JpaRepository<Repetition, Long> {
}
