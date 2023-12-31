package com.service.databaseservice.controller;

import com.service.databaseservice.payload.inc.learnsession.StatusTypeDTO;
import com.service.databaseservice.services.CardService;
import com.service.databaseservice.services.DeckService;
import com.service.databaseservice.services.PeekSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/db")
public class PeekSessionController {
    private final CardService cardService;
    private final DeckService deckService;
    private final PeekSessionService peekSessionService;

    public PeekSessionController(CardService cardService, DeckService deckService, PeekSessionService peekSessionService) {
        this.cardService = cardService;
        this.deckService = deckService;
        this.peekSessionService = peekSessionService;
    }

    @PostMapping("/users/{userId}/decks/{deckId}/peek-sessions")
    public ResponseEntity<Long> createPeekSession(@PathVariable Long userId, @PathVariable Long deckId) {
        if (!deckService.existsByDeckIdAndUserId(deckId, userId))
            return ResponseEntity.notFound().build();

        return peekSessionService.createPeekSession(userId, deckId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.internalServerError().build());
    }

    @PutMapping("/users/{userId}/peek-sessions/{peekSessionId}/status")
    public ResponseEntity<Void> updateLearnSessionStatus(@PathVariable Long userId, @PathVariable Long peekSessionId, @RequestBody StatusTypeDTO statusTypeDTO) { //sets the status of a peek-session
        if (!peekSessionService.doesPeekSessionFromUserExist(userId, peekSessionId))
            return ResponseEntity.notFound().build();

        return peekSessionService.updateStatusTypeInPeekSession(peekSessionId, statusTypeDTO)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.internalServerError().build();
    }

    @PostMapping("/users/{userId}/peek-sessions/{peekSessionId}/cards/{cardId}")
    public ResponseEntity<Void> createPeekSessionCard(@PathVariable Long userId, @PathVariable Long peekSessionId, @PathVariable Long cardId) {
        if (!peekSessionService.doesPeekSessionFromUserExist(userId, peekSessionId) || !cardService.doesCardBelongToOwner(cardId, userId))
            return ResponseEntity.notFound().build();

        return peekSessionService.savePeekSessionCard(peekSessionId, cardId, userId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.internalServerError().build();
    }

    @GetMapping("/users/{userId}/peek-sessions/{peekSessionId}/cards/random-card")
    public ResponseEntity<Object> getRandomCard(@PathVariable Long userId, @PathVariable Long peekSessionId) {
        if(!peekSessionService.doesPeekSessionFromUserExist(userId, peekSessionId))
            return ResponseEntity.notFound().build();

        return peekSessionService.getRandomCardToLearn(peekSessionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
