package com.service.databaseservice.repository;

import com.service.databaseservice.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DeckRepository extends JpaRepository<Deck, Long> {
    List<Deck> getAllByOwnerId(Long userId);
    Optional<Deck> getDeckByIdAndOwnerId(Long deckId, Long userId);
    Boolean existsDeckByIdAndOwnerId(Long deckId, Long userId);
    @Query("SELECT d FROM Deck d WHERE d.owner.id = ?1 AND d.name = ?2 ORDER BY d.id DESC LIMIT 1")
    Optional<Deck> findTopByOwnerIdAndNameOrderByIdDesc(Long ownerId, String name);
}
