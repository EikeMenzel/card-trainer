package com.service.userservice.controller;

import com.service.userservice.payload.inc.UserAccountInformationAchievementsDTO;
import com.service.userservice.payload.inc.UserAccountInformationDTO;
import com.service.userservice.services.DbQueryService;
import com.service.userservice.services.EmailValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/account")
public class UserController {
    private final DbQueryService dbQueryService;

    public UserController(DbQueryService dbQueryService) {
        this.dbQueryService = dbQueryService;
    }

    @GetMapping()
    @Operation(summary = "Retrieve User Account Information",
            description = "Fetches the account information and achievements for the specified user.<br><br>" +
                    "<strong>Note:</strong> Ensure the user ID is valid and the user exists in the database.<br><br>" +
                    "<strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of account information", content = @Content(schema = @Schema(implementation = UserAccountInformationAchievementsDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    public ResponseEntity<UserAccountInformationAchievementsDTO> getAccountInformation(
            @Parameter(description = "Unique identifier of the user", required = true) @RequestHeader Long userId) {
        Optional<UserAccountInformationDTO> userAccountInformationDTO = dbQueryService.getAccountInformation(userId);
        return userAccountInformationDTO.map(accountInformationDTO -> ResponseEntity.ok(new UserAccountInformationAchievementsDTO(
                        accountInformationDTO.getUsername(),
                        accountInformationDTO.getEmail(),
                        accountInformationDTO.getCardsToLearn(),
                        accountInformationDTO.getReceiveLearnNotification(),
                        accountInformationDTO.getLangCode(), accountInformationDTO.getLoginStreak(),
                        dbQueryService.getAchievementIds(userId))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping()
    @Operation(summary = "Update User Account Information",
            description = "Updates the account information for a specified user. Validates user ID, username length (4-30 characters), and email format.<br><br>" +
                    "<strong>Warning:</strong> Incorrect or invalid data will result in a bad request response." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful update of account information", content = @Content(schema = @Schema(implementation = UserAccountInformationDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<UserAccountInformationDTO> updateAccountInformation(
            @Parameter(description = "Unique identifier of the user", required = true) @RequestHeader Long userId,
            @RequestBody @Parameter(description = "Updated account information", required = true, schema = @Schema(implementation = UserAccountInformationDTO.class)) UserAccountInformationDTO userAccountInformationDTO) {

        if (userId < 0 || userAccountInformationDTO.getUsername().length() < 4 || userAccountInformationDTO.getUsername().length() > 30 || !EmailValidator.validate(userAccountInformationDTO.getEmail()))
            return ResponseEntity.badRequest().build();

        Pair<Optional<UserAccountInformationDTO>, HttpStatusCode> httpStatusPair = dbQueryService.updateAccountInformation(userId, userAccountInformationDTO);
        if (httpStatusPair.getRight().is2xxSuccessful()) {
            Optional<UserAccountInformationDTO> userAccountInformationDTOOptional = httpStatusPair.getLeft();
            var accountInformationDTO = userAccountInformationDTOOptional.orElse(null);
            return ResponseEntity.status(httpStatusPair.getRight()).body(accountInformationDTO);
        }
        return ResponseEntity.status(httpStatusPair.getRight()).build();
    }
}
