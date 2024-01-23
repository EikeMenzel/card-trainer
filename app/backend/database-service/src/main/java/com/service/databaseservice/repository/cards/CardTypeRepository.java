package com.service.databaseservice.repository.cards;

import com.service.databaseservice.model.cards.CardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardTypeRepository extends JpaRepository<CardType, Long> {
    Optional<CardType> getCardTypesByType(String type);
}
