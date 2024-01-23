package com.service.sessionservice.controller;

import com.service.sessionservice.payload.StatusTypeDTO;
import com.service.sessionservice.services.DbQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Create Peek Session",
            description = "Starts a new peek session for the specified deck and user.<br><br>" +
                    "<strong>Note:</strong> Requires a valid deck ID and user ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Peek session successfully created", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "User or deck not found"),
                    @ApiResponse(responseCode = "500", description = "Some service could not be reached")
            })
    public ResponseEntity<Long> createPeekSession(
            @Parameter(description = "User ID of the session creator", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID for the peek session", required = true) @PathVariable Long deckId) {
        Pair<HttpStatusCode, Long> httpStatusCodeLongPair = dbQueryService.savePeekSession(userId, deckId);
        return httpStatusCodeLongPair.getLeft() == HttpStatus.OK
                ? ResponseEntity.ok(httpStatusCodeLongPair.getRight())
                : ResponseEntity.status(httpStatusCodeLongPair.getLeft()).build();
    }

    @GetMapping("peek-sessions/{peekSessionId}/next-card") // Get next Card for peek-session
    @Operation(summary = "Get Next Card for Peek Session",
            description = "Retrieves the next random card for the ongoing peek session.<br><br>" +
                    "<strong>Note:</strong> Peek session ID and user ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Next card successfully retrieved", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "204", description = "No next card to be retrieved"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "User or deck not found"),
                    @ApiResponse(responseCode = "500", description = "Some service could not be reached")
            })
    public ResponseEntity<Object> getNextCard(
            @Parameter(description = "User ID of the learner", required = true) @RequestHeader Long userId,
            @Parameter(description = "Peek session ID for retrieving the next card", required = true) @PathVariable Long peekSessionId) {
        Pair<HttpStatusCode, Object> httpStatusCodeObjectPair = dbQueryService.getRandomPeekSessionCard(userId, peekSessionId);
        return httpStatusCodeObjectPair.getLeft() == HttpStatus.OK
                ? ResponseEntity.ok(httpStatusCodeObjectPair.getRight())
                : ResponseEntity.status(httpStatusCodeObjectPair.getLeft()).build();
    }

    @PostMapping("/peek-sessions/{peekSessionId}/cards/{cardId}")
    @Operation(summary = "Save Card in Peek Session",
            description = "Saves a card to the specified peek session.<br><br>" +
                    "<strong>Note:</strong> Requires valid peek session ID, user ID, and card ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Card successfully saved to peek session"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "User or peek-session or Card not found"),
                    @ApiResponse(responseCode = "500", description = "Some service could not be reached")
            })
    public ResponseEntity<Void> savePeekSessionCard(
            @Parameter(description = "User ID of the learner", required = true) @RequestHeader Long userId,
            @Parameter(description = "Peek session ID to which the card will be saved", required = true) @PathVariable Long peekSessionId,
            @Parameter(description = "Card ID to be saved in the peek-session", required = true) @PathVariable Long cardId) {
        return ResponseEntity.status(dbQueryService.savePeekSessionCard(userId, peekSessionId, cardId)).build();
    }

    @PutMapping("/peek-sessions/{peekSessionId}/status")
    @Operation(summary = "Update Peek Session Status",
            description = "Updates the status of an ongoing peek session.<br><br>" +
                    "<strong>Note:</strong> Valid peek session ID and user ID are required." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Peek session status updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "User or peek-session or Card not found"),
                    @ApiResponse(responseCode = "500", description = "Some service could not be reached")
            })
    public ResponseEntity<Void> updateLearnSessionStatus(@RequestHeader Long userId, @PathVariable Long peekSessionId, @RequestBody StatusTypeDTO statusTypeDTO) {
        return ResponseEntity.status(dbQueryService.updatePeekSessionStatus(userId, peekSessionId, statusTypeDTO)).build();
    }
}
