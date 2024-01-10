package com.service.databaseservice.controller;

import com.service.databaseservice.payload.out.AchievementDetailsDTO;
import com.service.databaseservice.services.UserAchievementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/db")
public class AchievementController {
    private final UserAchievementService userAchievementService;

    public AchievementController(UserAchievementService userAchievementService) {
        this.userAchievementService = userAchievementService;
    }

    @GetMapping("/achievements/{achievementId}")
    @Operation(summary = "Retrieve Achievement Details",
            description = "Fetches the details of a specific achievement by its ID.<br><br>" +
                    "<strong>Note:</strong> The achievement ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Achievement details successfully retrieved",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AchievementDetailsDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Achievement not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<AchievementDetailsDTO> getAchievementDetails(
            @Parameter(description = "ID of the achievement to retrieve details for", required = true) @PathVariable Long achievementId) {
        return userAchievementService.getAchievementDetails(achievementId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
