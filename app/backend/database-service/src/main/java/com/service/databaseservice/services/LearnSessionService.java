package com.service.databaseservice.services;

import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.User;
import com.service.databaseservice.model.sessions.LearnSession;
import com.service.databaseservice.model.sessions.StatusType;
import com.service.databaseservice.payload.inc.learnsession.RatingLevelDTO;
import com.service.databaseservice.payload.inc.learnsession.StatusTypeDTO;
import com.service.databaseservice.payload.out.HistoryDTO;
import com.service.databaseservice.payload.out.HistoryDetailDTO;
import com.service.databaseservice.repository.DeckRepository;
import com.service.databaseservice.repository.UserRepository;
import com.service.databaseservice.repository.sessions.LearnSessionRepository;
import com.service.databaseservice.repository.sessions.StatusTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LearnSessionService {
    private final LearnSessionRepository learnSessionRepository;
    private final StatusTypeRepository statusTypeRepository;
    private final UserRepository userRepository;
    private final DeckRepository deckRepository;
    private final Logger logger = LoggerFactory.getLogger(LearnSessionService.class);

    public LearnSessionService(LearnSessionRepository learnSessionRepository, StatusTypeRepository statusTypeRepository, UserRepository userRepository, DeckRepository deckRepository) {
        this.learnSessionRepository = learnSessionRepository;
        this.statusTypeRepository = statusTypeRepository;
        this.userRepository = userRepository;
        this.deckRepository = deckRepository;
    }

    public Optional<Timestamp> getLastLearnedFromLearnSessionById(Long deckId, Long userId) {
        Optional<LearnSession> learnSession = learnSessionRepository.findTopByDeckIdAndUserIdOrderByIdDesc(deckId, userId);
        return learnSession.map(session -> session.getFinishedAt() == null ? session.getCreatedAt() : session.getFinishedAt());
    }

    public List<HistoryDTO> getAllHistoryFromUserIdAndDeckId(Long userId, Long deckId) {
        return learnSessionRepository.getAllByUserIdAndDeckId(userId, deckId)
                .stream()
                .map(learnSession -> {
                    int cardsLearnedCount = learnSession.getRating1() + learnSession.getRating2() + learnSession.getRating3() + learnSession.getRating4() + learnSession.getRating5() + learnSession.getRating6();
                    return new HistoryDTO(learnSession.getId(), learnSession.getCreatedAt(), learnSession.getStatus().getType(), cardsLearnedCount);
                })
                .collect(Collectors.toList());
    }

    public Optional<HistoryDetailDTO> getHistoryDetailsFromHistoryIdAndUserId(Long historyId, Long userId, Long deckId) {
        return learnSessionRepository.getLearnSessionByIdAndUserIdAndDeckId(historyId, userId, deckId)
                .map(learnSession -> {
                    int cardsLearnedCount = learnSession.getRating1() + learnSession.getRating2() + learnSession.getRating3() + learnSession.getRating4() + learnSession.getRating5() + learnSession.getRating6();
                    return new HistoryDetailDTO(learnSession.getId(), learnSession.getDeck().getName(), learnSession.getCreatedAt(), learnSession.getFinishedAt(),
                            learnSession.getRating1(), learnSession.getRating2(), learnSession.getRating3(), learnSession.getRating4(), learnSession.getRating5(), learnSession.getRating6(),
                            learnSession.getStatus().getType(), cardsLearnedCount);
                });
    }

    @Transactional
    public Optional<Long> createLearnSession(Long userId, Long deckId) {
        try {
            Optional<StatusType> statusTypeOptional = statusTypeRepository.findById(1L); // Entry: Not Finished
            Optional<User> userOptional = userRepository.findById(userId);
            Optional<Deck> deckOptional = deckRepository.findById(deckId);

            if (statusTypeOptional.isEmpty() || userOptional.isEmpty() || deckOptional.isEmpty())
                return Optional.empty();

            var learnSession = learnSessionRepository.save(new LearnSession(userOptional.get(), deckOptional.get(), statusTypeOptional.get()));
            return learnSession.getId().describeConstable();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public boolean doesLearnSessionFromUserExist(Long userId, Long learnSessionId) {
        return learnSessionRepository.existsLearnSessionByIdAndUserId(learnSessionId, userId);
    }

    @Transactional
    public boolean updateRatingInLearnSession(Long learnSessionId, RatingLevelDTO ratingLevelDTO) {
        Optional<LearnSession> learnSessionOptional = learnSessionRepository.findById(learnSessionId);

        if (learnSessionOptional.isEmpty()) {
            return false;
        }

        var learnSession = learnSessionOptional.get();

        switch (ratingLevelDTO) {
            case RATING_0 -> learnSession.setRating1(learnSession.getRating1() + 1);
            case RATING_1 -> learnSession.setRating2(learnSession.getRating2() + 1);
            case RATING_2 -> learnSession.setRating3(learnSession.getRating3() + 1);
            case RATING_3 -> learnSession.setRating4(learnSession.getRating4() + 1);
            case RATING_4 -> learnSession.setRating5(learnSession.getRating5() + 1);
            case RATING_5 -> learnSession.setRating6(learnSession.getRating6() + 1);
            default ->  {
                return false;
            }
        }

        try {
            learnSessionRepository.save(learnSession);
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean updateStatusTypeInLearnSession(Long learnSessionId, StatusTypeDTO statusTypeDTO) {
        Optional<LearnSession> learnSessionOptional = learnSessionRepository.findById(learnSessionId);
        Optional<StatusType> statusTypeOptional = statusTypeRepository.findById(statusTypeDTO.getFieldId());

        if(learnSessionOptional.isEmpty() || statusTypeOptional.isEmpty())
            return false;

        try {
            learnSessionRepository.save(
                    learnSessionOptional.get()
                    .setLearnStatus(statusTypeOptional.get())
                    .setEndTimestamp()
            );
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Integer getLearnSessionCount(Long userId) {
        return Math.toIntExact(learnSessionRepository.getAllByUserId(userId)
                .stream()
                .filter(learnSession -> Objects.equals(learnSession.getStatus().getType(), "FINISHED"))
                .count());
    }

    public Integer getCardsLearnedCount(Long userId) {
        return learnSessionRepository.getAllByUserId(userId)
                .stream()
                .mapToInt(learnSession -> learnSession.getRating1() + learnSession.getRating2() + learnSession.getRating3() + learnSession.getRating4() + learnSession.getRating5() + learnSession.getRating6())
                .sum();
    }

    public boolean isDailyLearnSessionCompletedToday(Long userId) {
        return learnSessionRepository.isLearnSessionCompletedToday(userId);
    }

    public Integer getCardsLearnedToday(Long userId) {
        return learnSessionRepository.findLearnSessionsFinishedTodayByUserId(userId)
                .stream()
                .mapToInt(learnSession -> learnSession.getRating1() + learnSession.getRating2() + learnSession.getRating3() + learnSession.getRating4() + learnSession.getRating5() + learnSession.getRating6())
                .sum();
    }

    public Integer getCardsLearnedInThisSession(Long learnSessionId) {
        return learnSessionRepository.findById(learnSessionId)
                .stream()
                .mapToInt(learnSession -> learnSession.getRating1() + learnSession.getRating2() + learnSession.getRating3() + learnSession.getRating4() + learnSession.getRating5() + learnSession.getRating6())
                .sum();
    }
}
