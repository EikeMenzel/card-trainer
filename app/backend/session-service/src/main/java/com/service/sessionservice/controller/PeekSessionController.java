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
public class PeekSessionController {
    private final DbQueryService dbQueryService;

    public PeekSessionController(DbQueryService dbQueryService) {
        this.dbQueryService = dbQueryService;
    }

    @PostMapping("/decks/{deckId}/peek-sessions")
    public ResponseEntity<Long> createPeekSession(@RequestHeader Long userId, @PathVariable Long deckId) {
        Pair<HttpStatusCode, Long> httpStatusCodeLongPair = dbQueryService.savePeekSession(userId, deckId);
        return httpStatusCodeLongPair.getLeft() == HttpStatus.OK
                ? ResponseEntity.ok(httpStatusCodeLongPair.getRight())
                : ResponseEntity.status(httpStatusCodeLongPair.getLeft()).build();
    }

    @GetMapping("peek-sessions/{peekSessionId}/next-card") // Get next Card for peek-session
    public ResponseEntity<Object> getNextCard(@RequestHeader Long userId, @PathVariable Long peekSessionId) {
        Pair<HttpStatusCode, Object> httpStatusCodeObjectPair = dbQueryService.getRandomPeekSessionCard(userId, peekSessionId);
        return httpStatusCodeObjectPair.getLeft() == HttpStatus.OK
                ? ResponseEntity.ok(httpStatusCodeObjectPair.getRight())
                : ResponseEntity.status(httpStatusCodeObjectPair.getLeft()).build();
    }

    @PostMapping("/peek-sessions/{peekSessionId}/cards/{cardId}")
    public ResponseEntity<Void> savePeekSessionCard(@RequestHeader Long userId, @PathVariable Long peekSessionId, @PathVariable Long cardId) {
        return ResponseEntity.status(dbQueryService.savePeekSessionCard(userId, peekSessionId, cardId)).build();
    }

    @PutMapping("/peek-sessions/{peekSessionId}/status")
    public ResponseEntity<Void> updateLearnSessionStatus(@RequestHeader Long userId, @PathVariable Long peekSessionId, @RequestBody StatusTypeDTO statusTypeDTO) {
        return ResponseEntity.status(dbQueryService.updatePeekSessionStatus(userId, peekSessionId, statusTypeDTO)).build();
    }
}
