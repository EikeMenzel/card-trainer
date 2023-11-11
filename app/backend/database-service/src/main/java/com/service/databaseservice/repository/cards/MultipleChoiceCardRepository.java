package com.service.databaseservice.repository.cards;

import com.service.databaseservice.model.cards.MultipleChoiceCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultipleChoiceCardRepository extends JpaRepository<MultipleChoiceCard, Long> {
}
