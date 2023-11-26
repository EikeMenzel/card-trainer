package com.service.databaseservice.services;

import com.service.databaseservice.model.cards.Card;
import com.service.databaseservice.repository.cards.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
