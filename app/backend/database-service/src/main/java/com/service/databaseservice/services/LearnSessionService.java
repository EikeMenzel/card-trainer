package com.service.databaseservice.services;

import com.service.databaseservice.model.sessions.LearnSession;
import com.service.databaseservice.payload.out.HistoryDTO;
import com.service.databaseservice.repository.sessions.LearnSessionRepository;
import com.service.databaseservice.repository.sessions.StatusTypeRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LearnSessionService {
    private final LearnSessionRepository learnSessionRepository;
    private final StatusTypeRepository statusTypeRepository;
    public LearnSessionService(LearnSessionRepository learnSessionRepository, StatusTypeRepository statusTypeRepository) {
        this.learnSessionRepository = learnSessionRepository;
        this.statusTypeRepository = statusTypeRepository;
    }

    public Optional<Timestamp> getLastLearnedFromLearnSessionById(Long deckId, Long userId) {
        Optional<LearnSession> learnSession = learnSessionRepository.findTopByDeckIdAndUserIdOrderByIdDesc(deckId, userId);
        return learnSession.map(session -> session.getFinishedAt() == null ? session.getCreatedAt() : session.getFinishedAt());
    }

    public List<HistoryDTO> getAllHistoryFromUserId(Long userId) {
        return learnSessionRepository.getAllByUserId(userId)
                .stream()
                .map(learnSession -> {
                    int cardsLearnedCount = learnSession.getDifficulty1() + learnSession.getDifficulty2() + learnSession.getDifficulty3() + learnSession.getDifficulty4() + learnSession.getDifficulty5() + learnSession.getDifficulty6();
                    return new HistoryDTO(learnSession.getId(), learnSession.getCreatedAt(), learnSession.getStatus().getType(), cardsLearnedCount);
                })
                .collect(Collectors.toList());
    }
}
