package com.service.databaseservice.repository.sessions;

import com.service.databaseservice.model.sessions.PeekSessionCards;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeekSessionCardsRepository extends JpaRepository<PeekSessionCards, Long> {
    List<PeekSessionCards> getAllByPeekSessionId(Long peekSessionId);
}
