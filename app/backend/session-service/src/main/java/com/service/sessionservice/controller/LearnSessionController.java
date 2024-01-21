package com.service.sessionservice.controller;

import com.service.sessionservice.payload.RatingCardHandlerDTO;
import com.service.sessionservice.payload.StatusTypeDTO;
import com.service.sessionservice.services.AchievementQueryService;
import com.service.sessionservice.services.DbQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class LearnSessionController {
    private final DbQueryService dbQueryService;
    private final AchievementQueryService achievementQueryService;

    public LearnSessionController(DbQueryService dbQueryService, AchievementQueryService achievementQueryService) {
        this.dbQueryService = dbQueryService;
        this.achievementQueryService = achievementQueryService;
    }

    @PostMapping("/decks/{deckId}/learn-sessions")
    @Operation(summary = "Create Learn Session",
            description = "Starts a new learning session for the specified deck and user.<br><br>" +
                    "<strong>Note:</strong> Requires a valid deck ID and user ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Learn session successfully created", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "User or deck not found"),
                    @ApiResponse(responseCode = "500", description = "Some service could not be reached")
            })
    public ResponseEntity<Long> createLearnSession(
            @Parameter(description = "User ID of the session creator", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID for the learn session", required = true) @PathVariable Long deckId) {
        Pair<HttpStatusCode, Long> httpStatusCodeLongPair = dbQueryService.saveLearnSession(userId, deckId);
        return httpStatusCodeLongPair.getLeft() == HttpStatus.OK
                ? ResponseEntity.ok(httpStatusCodeLongPair.getRight())
                : ResponseEntity.status(httpStatusCodeLongPair.getLeft()).build();
    }

    @GetMapping("/decks/{deckId}/learn-sessions/{learnSessionId}/next-card") // Get next Card for learn-session
    @Operation(summary = "Get Next Card for Learning",
            description = "Retrieves the next card for the ongoing learning session in a specified deck.<br><br>" +
                    "<strong>Note:</strong> User ID and deck ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Next card successfully retrieved", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "204", description = "No next card to be retrieved"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "User or deck not found"),
                    @ApiResponse(responseCode = "409", description = "CardsToLearn reached"),
                    @ApiResponse(responseCode = "500", description = "Some service could not be reached")
            })
    public ResponseEntity<Object> getNextCard(
            @Parameter(description = "User ID of the learner", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID for retrieving the next card", required = true) @PathVariable Long deckId,
            @Parameter(description = "LearnSession ID for checking current amount of cards learned", required = true) @PathVariable Long learnSessionId) {
        Pair<HttpStatusCode, Object> httpStatusCodeObjectPair = dbQueryService.getLongestUnseenCard(userId, deckId, learnSessionId);
        if (httpStatusCodeObjectPair.getLeft() == HttpStatus.OK || httpStatusCodeObjectPair.getLeft() == HttpStatus.CONFLICT) {
            return ResponseEntity.status(httpStatusCodeObjectPair.getLeft()).body(httpStatusCodeObjectPair.getRight());
        }

        return ResponseEntity.status(httpStatusCodeObjectPair.getLeft()).build();
    }

    @PutMapping("/learn-sessions/{learnSessionId}/rating")
    @Operation(summary = "Update Session Difficulty",
            description = "Updates the difficulty rating of a card in an ongoing learn session.<br><br>" +
                    "<strong>Note:</strong> Learn session ID and user ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Learn session difficulty updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "User or Learn-session not found"),
                    @ApiResponse(responseCode = "500", description = "Some service could not be reached")
            })
    public ResponseEntity<Void> updateLearnSessionDifficulty(
            @Parameter(description = "User ID of the learner", required = true) @RequestHeader Long userId,
            @Parameter(description = "Learn session ID for updating difficulty", required = true) @PathVariable Long learnSessionId,
            @Parameter(description = "RatingDTO, with the rating", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RatingCardHandlerDTO.class))) @RequestBody RatingCardHandlerDTO ratingCardHandlerDTO) {
        var httpStatus = dbQueryService.updateLearnSessionDifficulty(userId, learnSessionId, ratingCardHandlerDTO);
        if (httpStatus.is2xxSuccessful())
            achievementQueryService.checkCardsLearnedAchievements(userId);

        return ResponseEntity.status(httpStatus).build();
    }

    @PutMapping("/learn-sessions/{learnSessionId}/status")
    @Operation(summary = "Update Learn Session Status",
            description = "Updates the status of an ongoing learn session.<br><br>" +
                    "<strong>Note:</strong> Valid learn session ID and user ID are required." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Learn session status updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "User or Learn-session not found"),
                    @ApiResponse(responseCode = "500", description = "Some service could not be reached")
            })
    public ResponseEntity<Void> updateLearnSessionStatus(
            @Parameter(description = "User ID of the learner", required = true) @RequestHeader Long userId,
            @Parameter(description = "Learn session ID for updating status", required = true) @PathVariable Long learnSessionId,
            @Parameter(description = "StatusTypeDTO - Status - Finished or Aborted", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatusTypeDTO.class))) @RequestBody StatusTypeDTO statusTypeDTO) {
        var httpStatus = dbQueryService.updateLearnSessionStatus(userId, learnSessionId, statusTypeDTO);
        if (httpStatus.is2xxSuccessful())
            achievementQueryService.checkSessionAchievements(userId);

        return ResponseEntity.status(httpStatus).build();
    }
}