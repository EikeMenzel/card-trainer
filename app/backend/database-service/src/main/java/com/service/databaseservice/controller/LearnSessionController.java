package com.service.databaseservice.controller;

import com.service.databaseservice.payload.inc.learnsession.RatingCardHandlerDTO;
import com.service.databaseservice.payload.inc.learnsession.StatusTypeDTO;
import com.service.databaseservice.payload.out.HistoryDTO;
import com.service.databaseservice.payload.out.HistoryDetailDTO;
import com.service.databaseservice.services.CardService;
import com.service.databaseservice.services.DeckService;
import com.service.databaseservice.services.LearnSessionService;
import com.service.databaseservice.services.RepetitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Get Last Learned Timestamp",
            description = "Retrieves the timestamp of the last learn session from a specific deck for a given user.<br><br>" +
                    "<strong>Note:</strong> Both user ID and deck ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Timestamp successfully retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Timestamp.class))),
                    @ApiResponse(responseCode = "404", description = "Learn session or user not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Timestamp> getLastLearnedFromDeckId(
            @Parameter(description = "User ID of the learner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID to retrieve the last learned timestamp from", required = true) @PathVariable Long deckId) {
        return learnSessionService.getLastLearnedFromLearnSessionById(deckId, userId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{userId}/decks/{deckId}/histories")
    @Operation(summary = "Get All Learn Sessions",
            description = "Retrieves all learn sessions for a given user from a specific deck.<br><br>" +
                    "<strong>Note:</strong> Both user ID and deck ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Learn sessions successfully retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HistoryDTO[].class))),
                    @ApiResponse(responseCode = "204", description = "No learn sessions or user found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<List<HistoryDTO>> getAllLearnSessionsFromUserId(
            @Parameter(description = "User ID of the learner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID to retrieve learn sessions from", required = true) @PathVariable Long deckId) {
        List<HistoryDTO> historyDTOS = learnSessionService.getAllHistoryFromUserIdAndDeckId(userId, deckId);
        return historyDTOS.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(historyDTOS);
    }

    @GetMapping("/users/{userId}/decks/{deckId}/histories/{historyId}")
    @Operation(summary = "Get Learn Session Details",
            description = "Retrieves detailed information about a specific learn session for a given user.<br><br>" +
                    "<strong>Note:</strong> User ID, deck ID, and history ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Learn session details successfully retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HistoryDetailDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Learn session or user not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<HistoryDetailDTO> getLearnSessionDetailsFromUserIdAndSessionId(
            @Parameter(description = "User ID of the learner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID associated with the learn session", required = true) @PathVariable Long deckId,
            @Parameter(description = "History ID of the learn session", required = true) @PathVariable Long historyId) {
        return learnSessionService.getHistoryDetailsFromHistoryIdAndUserId(historyId, userId, deckId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/users/{userId}/decks/{deckId}/learn-sessions")
    @Operation(summary = "Create Learn Session",
            description = "Starts a new learn session for a given user in a specific deck.<br><br>" +
                    "<strong>Note:</strong> Both user ID and deck ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Learn session successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))),
                    @ApiResponse(responseCode = "404", description = "Deck or user not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Long> createLearnSession(
            @Parameter(description = "User ID of the learner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID to start the learn session in", required = true) @PathVariable Long deckId) {
        if (!deckService.existsByDeckIdAndUserId(deckId, userId))
            return ResponseEntity.notFound().build();

        return learnSessionService.createLearnSession(userId, deckId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.internalServerError().build());
    }

    @PutMapping("/users/{userId}/learn-sessions/{learnSessionId}/rating")
    @Operation(summary = "Update Learn Session Rating",
            description = "Updates the rating of a card in a specific learn session for a user.<br><br>" +
                    "<strong>Note:</strong> Requires valid user ID, learn session ID, and card details." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Rating successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Learn session or card or user not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> updateLearnSessionRating( //increments the difficulty by one
                                                          @Parameter(description = "User ID of the learner", required = true) @PathVariable Long userId,
                                                          @Parameter(description = "Learn session ID to update rating for", required = true) @PathVariable Long learnSessionId,
                                                          @Parameter(description = "Contains the difficultyLevel that should be increased", required = true,
                                                                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = RatingCardHandlerDTO.class))) @RequestBody RatingCardHandlerDTO ratingCardHandlerDTO) {
        if (!learnSessionService.doesLearnSessionFromUserExist(userId, learnSessionId) || !cardService.doesCardBelongToOwner(ratingCardHandlerDTO.cardId(), userId))
            return ResponseEntity.notFound().build();

        return learnSessionService.updateRatingInLearnSession(learnSessionId, ratingCardHandlerDTO.ratingLevelDTO())
                && repetitionService.updateRepetition(ratingCardHandlerDTO.cardId(), ratingCardHandlerDTO.ratingLevelDTO())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.internalServerError().build();
    }

    @PutMapping("/users/{userId}/learn-sessions/{learnSessionId}/status")
    @Operation(summary = "Update Learn Session Status",
            description = "Updates the status of a specific learn session for a user.<br><br>" +
                    "<strong>Note:</strong> Requires valid user ID and learn session ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Learn session status successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Learn session or user not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> updateLearnSessionStatus( //sets the status of a learn-session
                                                          @Parameter(description = "User ID of the learner", required = true) @PathVariable Long userId,
                                                          @Parameter(description = "Learn session ID to update status for", required = true) @PathVariable Long learnSessionId,
                                                          @Parameter(description = "Contains the status-type that should be updated", required = true,
                                                                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatusTypeDTO.class))) @RequestBody StatusTypeDTO statusTypeDTO) {

        if (!learnSessionService.doesLearnSessionFromUserExist(userId, learnSessionId))
            return ResponseEntity.notFound().build();

        return learnSessionService.updateStatusTypeInLearnSession(learnSessionId, statusTypeDTO)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.internalServerError().build();
    }

    @GetMapping("/users/{userId}/decks/{deckId}/cards/longest-unseen")
    @Operation(summary = "Get Longest Unseen Card",
            description = "Retrieves the card that has been unseen for the longest time in a specific deck for a user.<br><br>" +
                    "<strong>Note:</strong> Requires valid user ID and deck ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Longest unseen card successfully retrieved", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "204", description = "No cards found"),
                    @ApiResponse(responseCode = "404", description = "Deck not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Object> getLongestUnseenCard(
            @Parameter(description = "User ID of the learner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID to retrieve the longest unseen card from", required = true) @PathVariable Long deckId) {
        if (!deckService.existsByDeckIdAndUserId(deckId, userId))
            return ResponseEntity.notFound().build();

        return cardService.getOldestCardToLearn(deckId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/users/{userId}/learn-sessions/count")
    @Operation(summary = "Get Learn Session Count",
            description = "Counts the total number of learn sessions completed by a user.<br><br>" +
                    "<strong>Note:</strong> User ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Learn session count successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Integer> getLearnSessionCountFromUser(
            @Parameter(description = "User ID to count learn sessions for", required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(learnSessionService.getLearnSessionCount(userId));
    }

    @GetMapping("/users/{userId}/cards-learned")
    @Operation(summary = "Get Cards Learned Amount",
            description = "Retrieves the total number of cards learned by a user.<br><br>" +
                    "<strong>Note:</strong> User ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cards learned amount successfully retrieved", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Integer> getCardsLearnedAmount(
            @Parameter(description = "User ID to retrieve cards learned amount for", required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(learnSessionService.getCardsLearnedCount(userId));
    }

    @GetMapping("/users/{userId}/learn-sessions/daily")
    @Operation(summary = "Get Daily Learn Session Status",
            description = "Checks if the user has completed their daily learn session.<br><br>" +
                    "<strong>Note:</strong> User ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Daily learn session completion status retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Boolean> getDailyLearnSession(
            @Parameter(description = "User ID to check the daily learn session status for", required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(learnSessionService.isDailyLearnSessionCompletedToday(userId));
    }

    @GetMapping("/users/{userId}/learn-sessions/cards-learned/daily")
    @Operation(summary = "Get Daily Cards Learned Amount",
            description = "Retrieves the total number of cards learned by the user on the current day.<br><br>" +
                    "<strong>Note:</strong> User ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Daily cards learned amount retrieved", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Integer> getCardsLearnedAmountDaily(
            @Parameter(description = "User ID to retrieve the daily cards learned amount for", required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(learnSessionService.getCardsLearnedToday(userId));
    }
}
