package com.service.databaseservice.services;

import com.service.databaseservice.model.cards.Card;
import com.service.databaseservice.payload.out.CardDTO;
import com.service.databaseservice.repository.cards.CardRepository;
import com.service.databaseservice.repository.cards.TextAnswerCardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final TextAnswerCardRepository textAnswerCardRepository;

    public CardService(CardRepository cardRepository, TextAnswerCardRepository textAnswerCardRepository) {
        this.cardRepository = cardRepository;
        this.textAnswerCardRepository = textAnswerCardRepository;
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

    @Transactional
    public boolean deleteCard(Long cardId) {
        if(cardRepository.getCardById(cardId).isEmpty())
            return false;

        cardRepository.deleteById(cardId);
        return true;
    }
}
