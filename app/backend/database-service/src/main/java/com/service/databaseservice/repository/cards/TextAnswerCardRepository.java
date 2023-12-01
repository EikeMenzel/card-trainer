package com.service.databaseservice.repository.cards;

import com.service.databaseservice.model.cards.TextAnswerCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TextAnswerCardRepository extends JpaRepository<TextAnswerCard, Long> {
    Optional<TextAnswerCard> getTextAnswerCardById(Long cardId);
}
