package com.service.databaseservice.controller;

import com.service.databaseservice.payload.out.UserDTO;
import com.service.databaseservice.payload.out.UserTokenDTO;
import com.service.databaseservice.services.UserTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/db/user-token")
public class UserTokenController {
    private final UserTokenService userTokenService;
    public UserTokenController(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }
    @PostMapping("/")
    @Operation(summary = "Create User Token",
            description = "Creates a new token for a user based on the provided token details.<br><br>" +
                    "<strong>Note:</strong> Requires a valid UserTokenDTO object." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User token successfully created"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> createUserToken(
            @Parameter(description = "The DTO to create an user-token", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))) @RequestBody UserTokenDTO userToken) {
        return userTokenService.createUserToken(userToken)
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.internalServerError().build();
    }
    @PutMapping("/{token}")
    @Operation(summary = "Update User with Token",
            description = "Updates user information based on a specific token, such as verifying user email.<br><br>" +
                    "<strong>Note:</strong> Requires a valid token. Only processes verification tokens." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Bad request, invalid token"),
                    @ApiResponse(responseCode = "409", description = "Conflict, user already verified"),
                    @ApiResponse(responseCode = "422", description = "Unprocessable entity, token type not supported"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> updateUserWithToken(
            @Parameter(description = "Token to process for user update", required = true) @PathVariable String token) {
        if(!userTokenService.isUserTokenValid(token))
            return ResponseEntity.badRequest().build();

        if(userTokenService.areTokenTypesIdentical(token, "VERIFICATION")) {
            if(userTokenService.isUserWithTokenVerified(token))
                return ResponseEntity.status(HttpStatus.CONFLICT).build();

            return userTokenService.setUserEmailAsVerified(token)
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping("/{token}/valid")
    @Operation(summary = "Check User Token Validity",
            description = "Checks if the provided token is valid.<br><br>" +
                    "<strong>Note:</strong> Requires a valid token." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Token is valid"),
                    @ApiResponse(responseCode = "400", description = "Bad request, invalid token"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Void> isUserTokenValid(
            @Parameter(description = "Token to check validity for", required = true) @PathVariable String token) {
        return userTokenService.isUserTokenValid(token)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.badRequest().build();
    }

}
