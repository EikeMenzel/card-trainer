package com.service.databaseservice.controller;

import com.service.databaseservice.services.UserLoginTrackerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/db")
public class UserLoginTrackerController {
    private final UserLoginTrackerService userLoginTrackerService;

    public UserLoginTrackerController(UserLoginTrackerService userLoginTrackerService) {
        this.userLoginTrackerService = userLoginTrackerService;
    }

    @PostMapping("/users/{userId}/user-login-tracker")
    @Operation(summary = "Save User Login",
            description = "Records a user login instance for the specified user.<br><br>" +
                    "<strong>Note:</strong> Requires a valid user ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User login successfully recorded"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> saveUserLogin(
            @Parameter(description = "User ID for whom the login is recorded", required = true) @PathVariable Long userId) {
        return ResponseEntity.status(userLoginTrackerService.saveUserLogin(userId)).build();
    }

    @GetMapping("users/{userId}/user-login-tracker")
    @Operation(summary = "Check Daily User Login",
            description = "Checks if the specified user has logged in today.<br><br>" +
                    "<strong>Note:</strong> Requires a valid user ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Daily login status retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Boolean> wasUserDailyLoggedIn(
            @Parameter(description = "User ID to check daily login status for", required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(userLoginTrackerService.wasUserLoggedInToday(userId));
    }
}
