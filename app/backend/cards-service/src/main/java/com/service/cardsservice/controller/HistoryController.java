package com.service.cardsservice.controller;

import com.service.cardsservice.payload.in.HistoryDTO;
import com.service.cardsservice.payload.in.HistoryDetailDTO;
import com.service.cardsservice.services.DbQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Retrieve All Histories",
            description = "Fetches all history entries associated with a specific user and deck.<br><br>" +
                    "<strong>Note:</strong> Ensure both the user ID and deck ID are valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "History entries successfully retrieved",
                            content = @Content(schema = @Schema(implementation = HistoryDTO[].class))),
                    @ApiResponse(responseCode = "204", description = "No content, no history entries found")
            })
    public ResponseEntity<List<HistoryDTO>> getAllHistoriesByUserIdAndDeckId(
            @Parameter(description = "User ID of the requester", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID to retrieve histories from", required = true) @PathVariable Long deckId) {
        List<HistoryDTO> historyDTOS = dbQueryService.getAllHistoriesByUserIdAndDeckId(userId, deckId);
        return historyDTOS.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(historyDTOS);
    }

    @GetMapping("/decks/{deckId}/histories/{historyId}")
    @Operation(summary = "Retrieve Specific History Detail",
            description = "Fetches detailed information of a specific history entry for a given user and deck.<br><br>" +
                    "<strong>Note:</strong> Ensure the history ID, user ID, and deck ID are valid and the user has access to the requested history." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "History detail successfully retrieved", content = @Content(schema = @Schema(implementation = HistoryDetailDTO.class))),
                    @ApiResponse(responseCode = "404", description = "History detail not found")
            })
    public ResponseEntity<HistoryDetailDTO> getHistoryDetailsByUserIdAndHistoryId(
            @Parameter(description = "User ID of the requester", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID from which the history is associated", required = true) @PathVariable Long deckId,
            @Parameter(description = "History ID of the detail to retrieve", required = true) @PathVariable Long historyId) {
        return dbQueryService.getDetailsHistoryByUserIdAndDeckIdAndHistoryId(userId, deckId, historyId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
