package com.service.databaseservice.repository.sessions;

import com.service.databaseservice.model.sessions.LearnSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LearnSessionRepository extends JpaRepository<LearnSession, Long> {
    Optional<LearnSession> findTopByDeckIdAndUserIdOrderByIdDesc(Long deckId, Long userId);
}
