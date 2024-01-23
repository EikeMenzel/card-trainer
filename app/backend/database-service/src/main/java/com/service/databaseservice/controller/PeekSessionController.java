package com.service.databaseservice.controller;

import com.service.databaseservice.payload.inc.learnsession.StatusTypeDTO;
import com.service.databaseservice.services.CardService;
import com.service.databaseservice.services.DeckService;
import com.service.databaseservice.services.PeekSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Create Peek Session",
            description = "Starts a new peek session for a given user in a specific deck.<br><br>" +
                    "<strong>Note:</strong> Requires valid user ID and deck ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Peek session successfully created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))),
                    @ApiResponse(responseCode = "404", description = "Deck or user not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Long> createPeekSession(
            @Parameter(description = "User ID of the learner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID to start the peek session in", required = true) @PathVariable Long deckId) {
        if (!deckService.existsByDeckIdAndUserId(deckId, userId))
            return ResponseEntity.notFound().build();

        return peekSessionService.createPeekSession(userId, deckId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.internalServerError().build());
    }

    @PutMapping("/users/{userId}/peek-sessions/{peekSessionId}/status")
    @Operation(summary = "Update Peek Session Status",
            description = "Updates the status of a specific peek session for a user.<br><br>" +
                    "<strong>Note:</strong> Requires valid user ID and peek session ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Peek session status successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Peek session or user not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> updateLearnSessionStatus( //sets the status of a peek-session
                                                          @Parameter(description = "User ID of the learner", required = true) @PathVariable Long userId,
                                                          @Parameter(description = "Peek session ID to update status for", required = true) @PathVariable Long peekSessionId,
                                                          @Parameter(description = "Contains the status-type that should be updated", required = true,
                                                                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatusTypeDTO.class))) @RequestBody StatusTypeDTO statusTypeDTO) {
        if (!peekSessionService.doesPeekSessionFromUserExist(userId, peekSessionId))
            return ResponseEntity.notFound().build();

        return peekSessionService.updateStatusTypeInPeekSession(peekSessionId, statusTypeDTO)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.internalServerError().build();
    }

    @PostMapping("/users/{userId}/peek-sessions/{peekSessionId}/cards/{cardId}")
    @Operation(summary = "Save Card in Peek Session",
            description = "Saves a card to a specific peek session for a user.<br><br>" +
                    "<strong>Note:</strong> Requires valid user ID, peek session ID, and card ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Card successfully saved to peek session"),
                    @ApiResponse(responseCode = "404", description = "Peek session or card or user not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> createPeekSessionCard(
            @Parameter(description = "User ID of the learner", required = true) @PathVariable Long userId,
            @Parameter(description = "Peek session ID to which the card will be saved", required = true) @PathVariable Long peekSessionId,
            @Parameter(description = "Card ID to be saved in the peek session", required = true) @PathVariable Long cardId) {
        if (!peekSessionService.doesPeekSessionFromUserExist(userId, peekSessionId) || !cardService.doesCardBelongToOwner(cardId, userId))
            return ResponseEntity.notFound().build();

        return peekSessionService.savePeekSessionCard(peekSessionId, cardId, userId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.internalServerError().build();
    }

    @GetMapping("/users/{userId}/peek-sessions/{peekSessionId}/cards/random-card")
    @Operation(summary = "Get Random Card from Peek Session",
            description = "Retrieves a random card from a specific peek session for a user.<br><br>" +
                    "<strong>Note:</strong> Requires valid user ID and peek session ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Random card successfully retrieved", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "204", description = "No cards available"),
                    @ApiResponse(responseCode = "404", description = "Peek session or user not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Object> createPeekSessionCard(
            @Parameter(description = "User ID of the learner", required = true) @PathVariable Long userId,
            @Parameter(description = "Peek session ID to which the card will be saved", required = true) @PathVariable Long peekSessionId) {
        if (!peekSessionService.doesPeekSessionFromUserExist(userId, peekSessionId))
            return ResponseEntity.notFound().build();

        return peekSessionService.getRandomCardToLearn(peekSessionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
