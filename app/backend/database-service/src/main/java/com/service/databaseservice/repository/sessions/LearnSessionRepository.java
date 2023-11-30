package com.service.databaseservice.repository.sessions;

import com.service.databaseservice.model.sessions.LearnSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LearnSessionRepository extends JpaRepository<LearnSession, Long> {
    Optional<LearnSession> findTopByDeckIdAndUserIdOrderByIdDesc(Long deckId, Long userId);
    List<LearnSession> getAllByUserId(Long userId);
    Optional<LearnSession> getLearnSessionByIdAndUserId(Long sessionId, Long userId);
}
