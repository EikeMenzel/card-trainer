package com.service.databaseservice.services;

import com.service.databaseservice.model.cards.Card;
import com.service.databaseservice.payload.out.CardDTO;
import com.service.databaseservice.repository.cards.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {
    private final CardRepository cardRepository;
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Integer getCardAmountFromDeckId(Long deckId) {
        return cardRepository.countCardsByDeckId(deckId);
    }

    public List<Card> getCardsByDeckId(Long deckId) {
        return cardRepository.getCardsByDeckId(deckId);
    }

    public List<CardDTO> getCardsFromDeckId(Long deckId) {
        return cardRepository.getCardsByDeckId(deckId)
                .stream()
                .map(card -> new CardDTO(card.getId(), card.getQuestion(), card.getCardType().getType()))
                .collect(Collectors.toList());
    }
}
