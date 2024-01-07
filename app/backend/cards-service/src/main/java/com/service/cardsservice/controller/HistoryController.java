package com.service.cardsservice.controller;

import com.service.cardsservice.payload.in.HistoryDTO;
import com.service.cardsservice.payload.in.HistoryDetailDTO;
import com.service.cardsservice.services.DbQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class HistoryController {
    private final DbQueryService dbQueryService;

    public HistoryController(DbQueryService dbQueryService) {
        this.dbQueryService = dbQueryService;
    }

    @GetMapping("/decks/{deckId}/histories")
    public ResponseEntity<List<HistoryDTO>> getAllHistoriesByUserIdAndDeckId(@RequestHeader Long userId, @PathVariable Long deckId) {
        List<HistoryDTO> historyDTOS = dbQueryService.getAllHistoriesByUserIdAndDeckId(userId, deckId);
        return historyDTOS.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(historyDTOS);
    }

    @GetMapping("/decks/{deckId}/histories/{historyId}")
    public ResponseEntity<HistoryDetailDTO> getHistoryDetailsByUserIdAndHistoryId(@RequestHeader Long userId, @PathVariable Long deckId, @PathVariable Long historyId) {
        return dbQueryService.getDetailsHistoryByUserIdAndDeckIdAndHistoryId(userId, deckId, historyId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
