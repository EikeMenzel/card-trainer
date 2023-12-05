package com.service.databaseservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.service.databaseservice.payload.out.CardDTO;
import com.service.databaseservice.services.CardService;
import com.service.databaseservice.services.DeckService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/db")
public class CardsController {
    private final DeckService deckService;
    private final CardService cardService;

    public CardsController(DeckService deckService, CardService cardService) {
        this.deckService = deckService;
        this.cardService = cardService;
    }

    @GetMapping("/users/{userId}/decks/{deckId}/cards")
    public ResponseEntity<?> getAllCardsByDeckIdAndUserId(@PathVariable Long userId, @PathVariable Long deckId) {
        if(!deckService.existsByDeckIdAndUserId(deckId, userId))
            return ResponseEntity.notFound().build();

        List<CardDTO> cardDTOList = cardService.getCardsFromDeckId(deckId);
        return cardDTOList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(cardDTOList);
    }

    @DeleteMapping("/users/{userId}/decks/{deckId}/cards/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable Long userId, @PathVariable Long deckId, @PathVariable Long cardId) {
        if(!deckService.existsByDeckIdAndUserId(deckId, userId))
            return ResponseEntity.notFound().build();

        return cardService.deleteCard(cardId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/users/{userId}/decks/{deckId}/cards")
    public ResponseEntity<?> saveCard(@PathVariable Long userId, @PathVariable Long deckId, @Valid @RequestBody JsonNode cardNode) { //TESTING
        if(!deckService.existsByDeckIdAndUserId(deckId, userId))
            return ResponseEntity.notFound().build();

        return cardService.saveCard(cardNode, userId, deckId)
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.internalServerError().build();
    }
}
