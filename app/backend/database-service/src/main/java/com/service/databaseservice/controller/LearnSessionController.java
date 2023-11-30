package com.service.databaseservice.controller;

import com.service.databaseservice.payload.out.HistoryDTO;
import com.service.databaseservice.payload.out.HistoryDetailDTO;
import com.service.databaseservice.services.LearnSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("api/v1/db")
public class LearnSessionController {
    private final LearnSessionService learnSessionService;
    public LearnSessionController(LearnSessionService learnSessionService) {
        this.learnSessionService = learnSessionService;
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

}
