package com.service.userservice.controller;

import com.service.userservice.payload.inc.UserAccountInformationAchievementsDTO;
import com.service.userservice.payload.inc.UserAccountInformationDTO;
import com.service.userservice.services.DbQueryService;
import com.service.userservice.services.EmailValidator;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
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

    @GetMapping("")
    public ResponseEntity<UserAccountInformationAchievementsDTO> getAccountInformation(@RequestHeader Long userId) {
        Optional<UserAccountInformationDTO> userAccountInformationDTO = dbQueryService.getAccountInformation(userId);
        return userAccountInformationDTO.map(accountInformationDTO -> ResponseEntity.ok(new UserAccountInformationAchievementsDTO(
                accountInformationDTO.getUsername(),
                accountInformationDTO.getEmail(),
                accountInformationDTO.getCardsToLearn(),
                accountInformationDTO.getReceiveLearnNotification(),
                accountInformationDTO.getLangCode(),
                dbQueryService.getAchievementIds(userId))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("")
    public ResponseEntity<UserAccountInformationDTO> updateAccountInformation(@RequestHeader Long userId, @RequestBody UserAccountInformationDTO userAccountInformationDTO) {
        if(userId < 0 || userAccountInformationDTO.getUsername().length() < 4 || userAccountInformationDTO.getUsername().length() > 30 || !EmailValidator.validate(userAccountInformationDTO.getEmail()))
            return ResponseEntity.badRequest().build();

        Pair<Optional<UserAccountInformationDTO>, HttpStatusCode> httpStatusPair = dbQueryService.updateAccountInformation(userId, userAccountInformationDTO);
        if(httpStatusPair.getRight().is2xxSuccessful())
            return ResponseEntity.status(httpStatusPair.getRight()).body(httpStatusPair.getLeft().get());
        return ResponseEntity.status(httpStatusPair.getRight()).build();
    }
}
