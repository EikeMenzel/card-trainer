package com.service.databaseservice.services;

import com.service.databaseservice.model.Repetition;
import com.service.databaseservice.repository.RepetitionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RepetitionService {
    private final RepetitionRepository repetitionRepository;

    public RepetitionService(RepetitionRepository repetitionRepository) {
        this.repetitionRepository = repetitionRepository;
    }

    public Optional<Repetition> getRepetitionByCardId(Long cardId) {
        return repetitionRepository.getRepetitionByCard_Id(cardId);
    }
}
