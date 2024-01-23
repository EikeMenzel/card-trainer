package com.service.databaseservice.controller;

import com.service.databaseservice.services.UserAchievementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/db/user-achievements")
public class UserAchievementsController {
    private final UserAchievementService userAchievementService;

    public UserAchievementsController(UserAchievementService userAchievementService) {
        this.userAchievementService = userAchievementService;
    }

    @GetMapping("/users/{userId}/achievements/ids")
    @Operation(summary = "Retrieve All Achievement IDs for User",
            description = "Fetches a list of all achievement IDs associated with the specified user.<br><br>" +
                    "<strong>Note:</strong> Requires a valid user ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of achievement IDs successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long[].class))),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<List<Long>> getAllAchievementsAsIdFromUser(
            @Parameter(description = "User ID to retrieve achievement IDs for", required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(userAchievementService.getAllUserAchievementIdsByUserId(userId));
    }

    @GetMapping("/users/{userId}/achievements/{achievementId}/exists")
    @Operation(summary = "Check User's Achievement",
            description = "Checks if the specified user has a specific achievement.<br><br>" +
                    "<strong>Note:</strong> Requires both valid user ID and achievement ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User has the achievement"),
                    @ApiResponse(responseCode = "404", description = "Achievement not found for user"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Void> doesUserHaveAchievement(
            @Parameter(description = "User ID to check the achievement for", required = true) @PathVariable Long userId,
            @Parameter(description = "Achievement ID to check for the user", required = true) @PathVariable Long achievementId) {
        return userAchievementService.doesUserHaveAchievement(userId, achievementId)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/users/{userId}/achievements")
    @Operation(summary = "Save Achievement for User",
            description = "Saves a specific achievement for the user.<br><br>" +
                    "<strong>Note:</strong> Requires valid user ID and achievement ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Achievement successfully saved for user"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> saveUserAchievement(
            @Parameter(description = "User ID to save the achievement for", required = true) @PathVariable Long userId,
            @Parameter(description = "Achievement ID to save the correct achievement", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)) )@RequestBody Long achievementId) {
        return userAchievementService.saveUserAchievement(userId, achievementId)
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.internalServerError().build();
    }
}
