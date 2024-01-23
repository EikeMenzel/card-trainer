package com.service.databaseservice.controller;

import com.service.databaseservice.payload.inc.UpdatePasswordDTO;
import com.service.databaseservice.payload.inc.UpdatePasswordDTOUnauthorized;
import com.service.databaseservice.payload.out.UserAccountInformationDTO;
import com.service.databaseservice.payload.out.UserDTO;
import com.service.databaseservice.payload.out.UserDailyReminderDTO;
import com.service.databaseservice.services.UserService;
import com.service.databaseservice.services.UserTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/db/users")
public class UserController {
    private final UserService userService;
    private final UserTokenService userTokenService;

    public UserController(UserService userService, UserTokenService userTokenService) {
        this.userService = userService;
        this.userTokenService = userTokenService;
    }

    @GetMapping("/{userId}/email")
    @Operation(summary = "Get User Email",
            description = "Retrieves the email of a user based on the user's ID.<br><br>" +
                    "<strong>Note:</strong> Requires a valid user ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<String> getUserEmailFromId(@PathVariable Long userId) {
        Optional<String> userEmail = userService.getUserEmailById(userId);
        return userEmail.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/{userId}/verified")
    @Operation(summary = "Check User Verification",
            description = "Checks if the specified user is verified.<br><br>" +
                    "<strong>Note:</strong> Requires a valid user ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Verification status retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    @SuppressWarnings("java:S5411") // @isUserVerified is already a primitive type
    public ResponseEntity<Boolean> getIsUserVerified(
            @Parameter(description = "User ID to check verification status for", required = true) @PathVariable Long userId) {
        return userService.isUserVerified(userId)
                ? ResponseEntity.ok(true)
                : ResponseEntity.notFound().build();
    }
    @GetMapping("/emails/{email}")
    @Operation(summary = "Get User by Email",
            description = "Retrieves user information based on the provided email.<br><br>" +
                    "<strong>Note:</strong> Requires a valid email address." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User information successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<UserDTO> getUserFromEmail(
            @Parameter(description = "Email to retrieve user information for", required = true) @PathVariable String email) {
        Optional<UserDTO> userDTO = userService.getUserByEmail(email);
        return userDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/emails/{email}/id")
    @Operation(summary = "Get User ID by Email",
            description = "Retrieves the user ID associated with the provided email address.<br><br>" +
                    "<strong>Note:</strong> Requires a valid email address." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User ID successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))),
                    @ApiResponse(responseCode = "404", description = "Email not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Long> getIdFromUserEmail(
            @Parameter(description = "Email to retrieve user ID for", required = true) @PathVariable String email) {
        var userId = userService.getUserIdFromEmail(email);
        return userId.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/emails/{email}/exists")
    @Operation(summary = "Check Email Existence",
            description = "Checks if the provided email exists in the system.<br><br>" +
                    "<strong>Note:</strong> Requires a valid email address." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email exists",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "404", description = "Email not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    @SuppressWarnings("java:S5411") // @doesEmailExist is already a primitive type
    public ResponseEntity<Boolean> doesEmailExist(
            @Parameter(description = "Email address to check existence for", required = true) @PathVariable String email) {
        return userService.doesEmailExist(email)
                ? ResponseEntity.ok(true)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/emails")
    @Operation(summary = "Get Emails for Daily Learn Reminder",
            description = "Retrieves a list of emails of users who have opted in for daily learn reminders." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email list successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDailyReminderDTO[].class))),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<List<UserDailyReminderDTO>> getEmailsOfUsersWithDailyLearnReminder() {
        return ResponseEntity.ok(userService.getEmailsOfUsersWithDailyLearnReminder());
    }
    @PostMapping("/")
    @Operation(summary = "Create User",
            description = "Creates a new user in the system with the provided user details.<br><br>" +
                    "<strong>Note:</strong> Requires a valid user object." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User successfully created"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> createUser(
            @Parameter(description = "The DTO to create an user", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))) @RequestBody UserDTO user) {
        return userService.createUser(user)
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{userId}/account")
    @Operation(summary = "Get User Account Information",
            description = "Retrieves account information for a specified user.<br><br>" +
                    "<strong>Note:</strong> Requires a valid user ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account information retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountInformationDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<UserAccountInformationDTO> getUserAccountInformation(
            @Parameter(description = "User ID to retrieve account information for", required = true) @PathVariable Long userId) {
        return userService.getAccountInformation(userId)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/account")
    @Operation(summary = "Update User Account Information",
            description = "Updates the account information for a specified user.<br><br>" +
                    "<strong>Note:</strong> Requires valid user ID and updated account information." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account information updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountInformationDTO.class))),
                    @ApiResponse(responseCode = "409", description = "Email already exists"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @SuppressWarnings("java:S5411") // @doesEmailExist is already a primitive type
    public ResponseEntity<UserAccountInformationDTO> updateUserAccountInformation(
            @Parameter(description = "User ID of the account to be updated", required = true) @PathVariable Long userId,
            @Parameter(description = "The DTO to update account information", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountInformationDTO.class))) @RequestBody UserAccountInformationDTO userAccountInformationDTO) {
        Optional<String> email = userService.getUserEmailById(userId);
        if(email.isPresent() && !email.get().equals(userAccountInformationDTO.getEmail()) && userService.doesEmailExist(userAccountInformationDTO.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        return userService.updateAccountInformation(userId, userAccountInformationDTO)
                ? ResponseEntity.ok(userAccountInformationDTO)
                : ResponseEntity.internalServerError().build();
    }

    @PutMapping("/{userId}/password")
    @Operation(summary = "Update User Password",
            description = "Updates the password for a specified user.<br><br>" +
                    "<strong>Note:</strong> Requires a valid user ID and the new password details." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Password successfully updated"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Void> updatePassword(
            @Parameter(description = "User ID for whom the password is to be updated", required = true) @PathVariable Long userId,
            @Parameter(description = "The DTO to update an password", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdatePasswordDTO.class))) @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return userService.updateUserPassword(userId, updatePasswordDTO.password())
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{userId}/password/unauthorized")
    @Operation(summary = "Update Password Unauthorized",
            description = "Updates the password for a specified user without requiring current user authentication.<br><br>" +
                    "<strong>Note:</strong> Requires a valid user ID, reset token, and the new password details." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Password successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Bad request, invalid token or token does not belong to user"),
                    @ApiResponse(responseCode = "404", description = "User or token not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Void> updatePasswordUnauthorized(
            @Parameter(description = "User ID for whom the password is to be updated", required = true) @PathVariable Long userId,
            @Parameter(description = "The DTO to update an password - without being logged in", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdatePasswordDTOUnauthorized.class))) @RequestBody UpdatePasswordDTOUnauthorized updatePasswordDTOUnauthorized) {
        if(!userTokenService.belongsTokenToUser(userId, updatePasswordDTOUnauthorized.token()))
            return ResponseEntity.badRequest().build();

        if(!userTokenService.isUserTokenValid(updatePasswordDTOUnauthorized.token()))
            return ResponseEntity.notFound().build();

        if(!userTokenService.areTokenTypesIdentical(updatePasswordDTOUnauthorized.token(), "PASSWORD_RESET"))
            return ResponseEntity.notFound().build();

        return userService.updateUserPassword(userId, updatePasswordDTOUnauthorized.password()) && userTokenService.deleteToken(userId, updatePasswordDTOUnauthorized.token())
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.notFound().build();
    }
}
