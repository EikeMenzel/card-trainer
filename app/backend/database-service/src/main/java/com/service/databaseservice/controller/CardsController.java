package com.service.databaseservice.controller;

import com.service.databaseservice.payload.out.CardDTO;
import com.service.databaseservice.services.CardService;
import com.service.databaseservice.services.DeckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
