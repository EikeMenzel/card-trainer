package com.service.achievementservice.controller;

import com.service.achievementservice.payload.AchievementDetailsDTO;
import com.service.achievementservice.services.AchievementService;
import com.service.achievementservice.services.DbQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AchievementController {
    private final AchievementService achievementService;
    private final DbQueryService dbQueryService;

    public AchievementController(AchievementService achievementService, DbQueryService dbQueryService) {
        this.achievementService = achievementService;
        this.dbQueryService = dbQueryService;
    }

    @PostMapping("/{userId}/achievements/check-deck")
    @Operation(summary = "Check Deck Achievements",
            description = "Triggers the check for deck-related achievements for the specified user.<br><br>" +
                    "<strong>Note:</strong> User ID must be valid." +
                    "<strong>⚠️ Warning:</strong> This method might not work correctly in Swagger UI due to internal routes which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deck achievements check initiated"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Server could not be reached")
            })
    public void checkDeckAchievements(@Parameter(description = "User ID for checking achievements", required = true) @PathVariable Long userId) {
        achievementService.achievementCreationDeck(userId);
    }

    @PostMapping("/{userId}/achievements/check-session")
    @Operation(summary = "Check Session Achievements",
            description = "Triggers the check for session-related achievements for the specified user.<br><br>" +
                    "<strong>Note:</strong> User ID must be valid." +
                    "<strong>⚠️ Warning:</strong> This method might not work correctly in Swagger UI due to internal routes which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deck achievements check initiated"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Server could not be reached")
            })
    public void checkSessionAchievements(@Parameter(description = "User ID for checking achievements", required = true) @PathVariable Long userId) {
        achievementService.achievementSessionDaily(userId);
        achievementService.achievementStateSessions(userId);
    }

    @PostMapping("/{userId}/achievements/check-cards-learned")
    @Operation(summary = "Check Cards Learned Achievements",
            description = "Triggers the check for cards learned achievements for the specified user.<br><br>" +
                    "<strong>Note:</strong> User ID must be valid." +
                    "<strong>⚠️ Warning:</strong> This method might not work correctly in Swagger UI due to internal routes which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deck achievements check initiated"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Server could not be reached")
            })
    public void checkCardsLearnedAchievements(@Parameter(description = "User ID for checking achievements", required = true) @PathVariable Long userId) {
        achievementService.achievementCardsLearnedDaily(userId);
        achievementService.achievementCardsLearned(userId);
    }

    @PostMapping("/{userId}/achievements/check-daily-login")
    @Operation(summary = "Check Daily Login Achievement",
            description = "Triggers the check for the daily login achievement for the specified user.<br><br>" +
                    "<strong>Note:</strong> User ID must be valid." +
                    "<strong>⚠️ Warning:</strong> This method might not work correctly in Swagger UI due to internal routes which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deck achievements check initiated"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Server could not be reached")
            })
    public void checkDailyLoginAchievement(@Parameter(description = "User ID for checking achievements", required = true) @PathVariable Long userId) {
        achievementService.achievementDailyLogin(userId);
    }

    @GetMapping("/achievements/{achievementId}")
    @Operation(summary = "Retrieve Achievement Details",
            description = "Fetches the details of a specific achievement by its ID.<br><br>" +
                    "<strong>Note:</strong> The achievement ID must be valid. User ID is required for authorization purposes." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Achievement details successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AchievementDetailsDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Achievement not found"),
                    @ApiResponse(responseCode = "500", description = "Server could not be reached")
            })
    public ResponseEntity<AchievementDetailsDTO> getAchievementDetails(
            @Parameter(description = "User ID of the requester", required = true) @RequestHeader Long userId,
            @Parameter(description = "ID of the achievement to retrieve details for", required = true) @PathVariable Long achievementId) {
        return dbQueryService.getAchievementDetails(achievementId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
