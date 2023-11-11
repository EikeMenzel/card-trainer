package com.service.databaseservice.repository.cards;

import com.service.databaseservice.model.cards.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
