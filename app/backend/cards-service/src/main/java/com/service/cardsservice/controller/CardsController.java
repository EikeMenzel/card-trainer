package com.service.cardsservice.controller;

import com.service.cardsservice.services.DbQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CardsController {
    private final DbQueryService dbQueryService;

    public CardsController(DbQueryService dbQueryService) {
        this.dbQueryService = dbQueryService;
    }

    @DeleteMapping("/decks/{deckId}/cards/{cardId}")
    public ResponseEntity<?> deleteCard(@RequestHeader Long userId, @PathVariable Long deckId, @PathVariable Long cardId) {
        return ResponseEntity.status(dbQueryService.deleteCard(userId, deckId, cardId)).build();
    }
}
