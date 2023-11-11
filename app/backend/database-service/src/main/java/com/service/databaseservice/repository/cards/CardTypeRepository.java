package com.service.databaseservice.repository.cards;

import com.service.databaseservice.model.cards.CardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardTypeRepository extends JpaRepository<CardType, Long> {
}
