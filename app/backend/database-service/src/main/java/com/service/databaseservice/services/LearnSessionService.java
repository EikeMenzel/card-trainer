package com.service.databaseservice.services;

import com.service.databaseservice.model.sessions.LearnSession;
import com.service.databaseservice.repository.sessions.LearnSessionRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class LearnSessionService {
    private final LearnSessionRepository learnSessionRepository;

    public LearnSessionService(LearnSessionRepository learnSessionRepository) {
        this.learnSessionRepository = learnSessionRepository;
    }

    public Optional<Timestamp> getLastLearnedFromLearnSessionById(Long deckId, Long userId) {
        Optional<LearnSession> learnSession = learnSessionRepository.findTopByDeckIdAndUserIdOrderByIdDesc(deckId, userId);
        return learnSession.map(session -> session.getFinishedAt() == null ? session.getCreatedAt() : session.getFinishedAt());
    }
}
