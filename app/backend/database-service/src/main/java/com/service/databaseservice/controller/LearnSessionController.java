package com.service.databaseservice.controller;

import com.service.databaseservice.payload.inc.learnsession.RatingCardHandlerDTO;
import com.service.databaseservice.payload.inc.learnsession.StatusTypeDTO;
import com.service.databaseservice.payload.out.HistoryDTO;
import com.service.databaseservice.payload.out.HistoryDetailDTO;
import com.service.databaseservice.services.CardService;
import com.service.databaseservice.services.DeckService;
import com.service.databaseservice.services.LearnSessionService;
import com.service.databaseservice.services.RepetitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("api/v1/db")
public class LearnSessionController {
    private final LearnSessionService learnSessionService;
    private final DeckService deckService;
    private final CardService cardService;
    private final RepetitionService repetitionService;
    public LearnSessionController(LearnSessionService learnSessionService, DeckService deckService, CardService cardService, RepetitionService repetitionService) {
        this.learnSessionService = learnSessionService;
        this.deckService = deckService;
        this.cardService = cardService;
        this.repetitionService = repetitionService;
    }
    @GetMapping("/users/{userId}/learn-sessions/{deckId}/timestamp")
    public ResponseEntity<Timestamp> getLastLearnedFromDeckId(@PathVariable Long userId, @PathVariable Long deckId) {
        return learnSessionService.getLastLearnedFromLearnSessionById(deckId, userId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{userId}/histories")
    public ResponseEntity<List<HistoryDTO>> getAllLearnSessionsFromUserId(@PathVariable Long userId) {
        List<HistoryDTO> historyDTOS = learnSessionService.getAllHistoryFromUserId(userId);
        return historyDTOS.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(historyDTOS);
    }

    @GetMapping("/users/{userId}/histories/{historyId}")
    public ResponseEntity<HistoryDetailDTO> getLearnSessionDetailsFromUserIdAndSessionId(@PathVariable Long userId, @PathVariable Long historyId) {
        return learnSessionService.getHistoryDetailsFromHistoryIdAndUserId(historyId, userId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/users/{userId}/decks/{deckId}/learn-sessions")
    public ResponseEntity<?> createLearnSession(@PathVariable Long userId, @PathVariable Long deckId) {
        if(!deckService.existsByDeckIdAndUserId(deckId, userId))
            return ResponseEntity.notFound().build();

        return learnSessionService.createLearnSession(userId, deckId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.internalServerError().build());
    }

    @PutMapping("/users/{userId}/learn-sessions/{learnSessionId}/rating")
    public ResponseEntity<?> updateLearnSessionRating(@PathVariable Long userId, @PathVariable Long learnSessionId, @RequestBody RatingCardHandlerDTO ratingCardHandlerDTO) { //increments the difficulty by one
        if(!learnSessionService.doesLearnSessionFromUserExist(userId, learnSessionId) || !cardService.doesCardBelongToOwner(ratingCardHandlerDTO.cardId(), userId))
            return ResponseEntity.notFound().build();

        return learnSessionService.updateRatingInLearnSession(learnSessionId, ratingCardHandlerDTO.ratingLevelDTO())
                && repetitionService.updateRepetition(ratingCardHandlerDTO.cardId(), ratingCardHandlerDTO.ratingLevelDTO())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.internalServerError().build();
    }

    @PutMapping("/users/{userId}/learn-sessions/{learnSessionId}/status")
    public ResponseEntity<?> updateLearnSessionStatus(@PathVariable Long userId, @PathVariable Long learnSessionId, @RequestBody StatusTypeDTO statusTypeDTO) { //sets the status of a learn-session
        if(!learnSessionService.doesLearnSessionFromUserExist(userId, learnSessionId))
            return ResponseEntity.notFound().build();

        return learnSessionService.updateStatusTypeInLearnSession(learnSessionId, statusTypeDTO)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.internalServerError().build();
    }

    //TODO Switch to another location? Up2Discussion
    @GetMapping("/users/{userId}/decks/{deckId}/cards/longest-unseen")
    public ResponseEntity<Object> getLongestUnseenCard(@PathVariable Long userId, @PathVariable Long deckId) {
        if(!deckService.existsByDeckIdAndUserId(deckId, userId))
            return ResponseEntity.notFound().build();

        return cardService.getOldestCardToLearn(deckId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }
}
