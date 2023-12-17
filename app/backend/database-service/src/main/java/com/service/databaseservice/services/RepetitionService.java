package com.service.databaseservice.services;

import com.service.databaseservice.model.Repetition;
import com.service.databaseservice.model.User;
import com.service.databaseservice.model.cards.Card;
import com.service.databaseservice.payload.inc.learnsession.RatingLevelDTO;
import com.service.databaseservice.repository.RepetitionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class RepetitionService {
    private final RepetitionRepository repetitionRepository;
    private final Logger logger = LoggerFactory.getLogger(RepetitionService.class);

    public RepetitionService(RepetitionRepository repetitionRepository) {
        this.repetitionRepository = repetitionRepository;
    }

    public Optional<Repetition> getRepetitionByCardId(Long cardId) {
        return repetitionRepository.getRepetitionByCard_Id(cardId);
    }

    public boolean initRepetition(Card card, User user) {
        if(card == null || user == null)
            return false;
        try {
            repetitionRepository.save(new Repetition(0, -1, 2.5, 0, Timestamp.from(Instant.now()), user, card));
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public boolean updateRepetition(Long cardId, RatingLevelDTO ratingLevelDTO) {
        Optional<Repetition> repetitionOptional = repetitionRepository.getRepetitionByCard_Id(cardId);

        if(repetitionOptional.isEmpty())
            return false;

        try {
            repetitionRepository.save(SM2Algorithm.calc(repetitionOptional.get(), ratingLevelDTO));
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }
}
