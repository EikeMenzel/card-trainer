package com.service.sessionservice.controller;

import com.service.sessionservice.payload.RatingCardHandlerDTO;
import com.service.sessionservice.payload.StatusTypeDTO;
import com.service.sessionservice.services.DbQueryService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class LearnSessionController {
    private final DbQueryService dbQueryService;

    public LearnSessionController(DbQueryService dbQueryService) {
        this.dbQueryService = dbQueryService;
    }

    @PostMapping("/decks/{deckId}/learn-sessions")
    public ResponseEntity<Long> createLearnSession(@RequestHeader Long userId, @PathVariable Long deckId) {
        Pair<HttpStatusCode, Long> httpStatusCodeLongPair = dbQueryService.saveLearnSession(userId, deckId);
        return httpStatusCodeLongPair.getLeft() == HttpStatus.OK
                ? ResponseEntity.ok(httpStatusCodeLongPair.getRight())
                : ResponseEntity.status(httpStatusCodeLongPair.getLeft()).build();
    }

    @GetMapping("/decks/{deckId}/next-card") // Get next Card for learn-session
    public ResponseEntity<Object> getNextCard(@RequestHeader Long userId, @PathVariable Long deckId) {
        Pair<HttpStatusCode, Object> httpStatusCodeObjectPair = dbQueryService.getLongestUnseenCard(userId, deckId);
        return httpStatusCodeObjectPair.getLeft() == HttpStatus.OK
                ? ResponseEntity.ok(httpStatusCodeObjectPair.getRight())
                : ResponseEntity.status(httpStatusCodeObjectPair.getLeft()).build();
    }

    @PutMapping("/learn-sessions/{learnSessionId}/rating")
    public ResponseEntity<?> updateLearnSessionDifficulty(@RequestHeader Long userId, @PathVariable Long learnSessionId, @RequestBody RatingCardHandlerDTO ratingCardHandlerDTO) {
        return ResponseEntity.status(dbQueryService.updateLearnSessionDifficulty(userId, learnSessionId, ratingCardHandlerDTO)).build();
    }

    @PutMapping("/learn-sessions/{learnSessionId}/status")
    public ResponseEntity<?> updateLearnSessionDifficulty(@RequestHeader Long userId, @PathVariable Long learnSessionId, @RequestBody StatusTypeDTO statusTypeDTO) {
        return ResponseEntity.status(dbQueryService.updateLearnSessionStatus(userId, learnSessionId, statusTypeDTO)).build();
    }

}
