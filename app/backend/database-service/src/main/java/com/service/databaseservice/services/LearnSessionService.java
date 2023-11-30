package com.service.databaseservice.services;

import com.service.databaseservice.model.sessions.LearnSession;
import com.service.databaseservice.payload.out.HistoryDTO;
import com.service.databaseservice.payload.out.HistoryDetailDTO;
import com.service.databaseservice.repository.sessions.LearnSessionRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<HistoryDTO> getAllHistoryFromUserId(Long userId) {
        return learnSessionRepository.getAllByUserId(userId)
                .stream()
                .map(learnSession -> {
                    int cardsLearnedCount = learnSession.getDifficulty1() + learnSession.getDifficulty2() + learnSession.getDifficulty3() + learnSession.getDifficulty4() + learnSession.getDifficulty5() + learnSession.getDifficulty6();
                    return new HistoryDTO(learnSession.getId(), learnSession.getCreatedAt(), learnSession.getStatus().getType(), cardsLearnedCount);
                })
                .collect(Collectors.toList());
    }

    public Optional<HistoryDetailDTO> getHistoryDetailsFromHistoryIdAndUserId(Long historyId, Long userId) {
        return learnSessionRepository.getLearnSessionByIdAndUserId(historyId, userId).map(learnSession -> {
            int cardsLearnedCount = learnSession.getDifficulty1() + learnSession.getDifficulty2() + learnSession.getDifficulty3() + learnSession.getDifficulty4() + learnSession.getDifficulty5() + learnSession.getDifficulty6();
            return new HistoryDetailDTO(learnSession.getId(), learnSession.getDeck().getName(), learnSession.getCreatedAt(), learnSession.getFinishedAt(),
                    learnSession.getDifficulty1(), learnSession.getDifficulty2(), learnSession.getDifficulty3(), learnSession.getDifficulty4(), learnSession.getDifficulty5(), learnSession.getDifficulty6(),
                    learnSession.getStatus().getType(), cardsLearnedCount);
        });
    }
}
