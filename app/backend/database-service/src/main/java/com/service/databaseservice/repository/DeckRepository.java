package com.service.databaseservice.repository;

import com.service.databaseservice.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeckRepository extends JpaRepository<Deck, Long> {
    List<Deck> getAllByOwnerId(Long userId);
    Optional<Deck> getDeckByIdAndOwnerId(Long deckId, Long userId);
    Boolean existsDeckByIdAndOwnerId(Long deckId, Long userId);
}
