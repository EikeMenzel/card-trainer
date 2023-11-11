package com.service.databaseservice.repository.cards;

import com.service.databaseservice.model.cards.TextAnswerCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextAnswerCardRepository extends JpaRepository<TextAnswerCard, Long> {
}
