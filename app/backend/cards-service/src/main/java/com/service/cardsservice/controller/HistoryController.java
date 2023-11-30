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

    @GetMapping("/histories")
    public ResponseEntity<List<HistoryDTO>> getAllHistoriesByUserId(@RequestHeader Long userId) {
        List<HistoryDTO> historyDTOS = dbQueryService.getAllHistoriesByUserId(userId);
        return historyDTOS.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(historyDTOS);
    }

    @GetMapping("/histories/{historyId}")
    public ResponseEntity<HistoryDetailDTO> getHistoryDetailsByUserIdAndHistoryId(@RequestHeader Long userId, @PathVariable Long historyId) {
        return dbQueryService.getDetailsHistoryByUserIdAndHistoryId(userId, historyId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
