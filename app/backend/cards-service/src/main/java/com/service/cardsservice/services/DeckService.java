package com.service.cardsservice.services;

import com.service.cardsservice.payload.out.DeckDetailInformationDTO;
import com.service.cardsservice.payload.out.DeckInformationDTO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeckService {
    private final DbQueryService dbQueryService;

    public DeckService(DbQueryService dbQueryService) {
        this.dbQueryService = dbQueryService;
    }

    public List<DeckInformationDTO> getAllDeckInformation(Long userId) {
        return dbQueryService.getAllDecksByUserId(userId)
                .stream()
                .map(deckDTO -> {
                    Integer cardsAmount = dbQueryService.getCardsAmountByDeck(userId, deckDTO.deckId());
                    Integer cardsToLearn = dbQueryService.getCardsToLearnAmountByDeck(userId, deckDTO.deckId());
                    Timestamp lastLearned = dbQueryService.getLastLearnedTimestampByDeck(userId, deckDTO.deckId()).orElse(null);
                    return new DeckInformationDTO(deckDTO.deckId(), deckDTO.deckName(), cardsAmount, cardsToLearn, lastLearned);
                })
                .collect(Collectors.toList());
    }

    public Optional<DeckDetailInformationDTO> getDetailInformationDeck(Long userId, Long deckId) {
        return dbQueryService.getDeckByUserIdAndDeckId(userId, deckId)
                .map(deckDTO -> {
                    Integer cardsAmount = dbQueryService.getCardsAmountByDeck(userId, deckId);
                    Integer cardsToLearn = dbQueryService.getCardsToLearnAmountByDeck(userId, deckId);
                    Timestamp lastLearned = dbQueryService.getLastLearnedTimestampByDeck(userId, deckId).orElse(null);
                    List<Integer> getDeckLearnState = dbQueryService.getLearnStateOfDeck(userId, deckId);
                    return new DeckDetailInformationDTO(deckDTO.deckId(), deckDTO.deckName(), cardsAmount, cardsToLearn, lastLearned, getDeckLearnState);
                });
    }

    public Integer getDeckSize(Long userId, Long deckId) {
        return dbQueryService.getCardsAmountByDeck(userId, deckId);
    }
}
