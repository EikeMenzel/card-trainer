package com.service.databaseservice.repository.sessions;

import com.service.databaseservice.model.sessions.PeekSessionCards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeekSessionCardsRepository extends JpaRepository<PeekSessionCards, Long> {
}
