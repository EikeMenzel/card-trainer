package com.service.databaseservice.repository.cards;

import com.service.databaseservice.model.cards.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Integer countCardsByDeckId(Long deckId);
    List<Card> getCardsByDeckId(Long deckId);
    Optional<Card> getCardById(Long cardId);
}
