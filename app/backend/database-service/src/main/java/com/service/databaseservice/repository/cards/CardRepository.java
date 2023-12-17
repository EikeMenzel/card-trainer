package com.service.databaseservice.repository.cards;

import com.service.databaseservice.model.cards.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Integer countCardsByDeckId(Long deckId);
    List<Card> getCardsByDeckId(Long deckId);
    Optional<Card> getCardById(Long cardId);

    @Query("SELECT c FROM Card c JOIN Repetition r ON c.id = r.card.id WHERE c.deck.id = :deckId ORDER BY r.nextLearnTimestamp ASC LIMIT 1")
    Optional<Card> findOldestCardToLearn(@Param("deckId") Long deckId);
}
