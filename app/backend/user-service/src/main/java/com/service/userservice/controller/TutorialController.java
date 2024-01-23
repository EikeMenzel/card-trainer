package com.service.userservice.controller;

import com.service.userservice.payload.inc.TutorialPage;
import com.service.userservice.services.DbQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tutorials")
public class TutorialController {
    private final DbQueryService dbQueryService;

    public TutorialController(DbQueryService dbQueryService) {
        this.dbQueryService = dbQueryService;
    }

    @GetMapping("/{tutorialPage}")
    @Operation(summary = "Check User's Access to a Tutorial Page",
            description = "Determines if a specific user, identified by a header, has access to a particular tutorial page.<br><br>" +
                    "<strong>Note:</strong> This endpoint is used to verify if a user has already accessed or been assigned a specific tutorial page. " +
                    "It's part of the user's tutorial tracking system." +
                    "<br><br><strong>⚠️ Warning:</strong> Responses are status codes only without content." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User has access to the tutorial page (No Content)",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Tutorial page not found for the user")
            })
    public ResponseEntity<Void> doesUserHaveTutorialPage(
            @Parameter(description = "Unique ID of the user", required = true) @RequestHeader Long userId,
            @Parameter(description = "Tutorial page to check for the user", required = true, schema = @Schema(implementation = TutorialPage.class)) @PathVariable TutorialPage tutorialPage) {
        return dbQueryService.doesUserHaveTutorialPage(userId, tutorialPage)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/{tutorialPage}")
    @Operation(summary = "Save User's Tutorial Page Access",
            description = "Records a user's access to a specific tutorial page, identified by a header.<br><br>" +
                    "<strong>Note:</strong> This endpoint is used to save a record when a user accesses a new tutorial page. " +
                    "It helps in tracking the tutorial progress of the user." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work as expected if user data constraints are violated." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tutorial page access successfully recorded for the user",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error, possibly due to database constraints or connectivity issues")
            })
    public ResponseEntity<Void> saveUserTutorialPage(
            @Parameter(description = "Unique ID of the user", required = true) @RequestHeader Long userId,
            @Parameter(description = "Tutorial page to check for the user", required = true, schema = @Schema(implementation = TutorialPage.class)) @PathVariable TutorialPage tutorialPage) {
        return ResponseEntity.status(dbQueryService.saveTutorialPage(userId, tutorialPage)).build();
    }
}
