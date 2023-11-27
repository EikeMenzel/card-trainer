package com.service.databaseservice.controller;

import com.service.databaseservice.model.User;
import com.service.databaseservice.payload.inc.UpdatePasswordDTO;
import com.service.databaseservice.payload.out.UserAccountInformationDTO;
import com.service.databaseservice.payload.out.UserDTO;
import com.service.databaseservice.payload.out.UserDailyReminderDTO;
import com.service.databaseservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/db/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}/email")
    public ResponseEntity<String> getUserEmailFromId(@PathVariable Long userId) {
        Optional<String> userEmail = userService.getUserEmailById(userId);
        return userEmail.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/{userId}/verified")
    public ResponseEntity<Boolean> getIsUserVerified(@PathVariable Long userId) {
        return userService.isUserVerified(userId)
                ? ResponseEntity.ok(true)
                : ResponseEntity.notFound().build();
    }
    @GetMapping("/emails/{email}")
    public ResponseEntity<UserDTO> getUserFromEmail(@PathVariable String email) {
        Optional<UserDTO> userDTO = userService.getUserByEmail(email);
        return userDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/emails/{email}/id")
    public ResponseEntity<Long> getIdFromUserEmail(@PathVariable String email) {
        var userId = userService.getUserIdFromEmail(email);
        return userId.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/emails/{email}/exists")
    public ResponseEntity<Boolean> doesEmailExist(@PathVariable String email) {
        return userService.doesEmailExist(email)
                ? ResponseEntity.ok(true)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/emails")
    public ResponseEntity<List<UserDailyReminderDTO>> getEmailsOfUsersWithDailyLearnReminder() {
        return ResponseEntity.ok(userService.getEmailsOfUsersWithDailyLearnReminder());
    }
    @PostMapping("/")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO user) {
        return userService.createUser(user)
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{userId}/account")
    public ResponseEntity<UserAccountInformationDTO> getUserAccountInformation(@PathVariable Long userId) {
        return userService.getAccountInformation(userId)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/account")
    public ResponseEntity<UserAccountInformationDTO> updateUserAccountInformation(@PathVariable Long userId, @RequestBody UserAccountInformationDTO userAccountInformationDTO) {
        Optional<String> email = userService.getUserEmailById(userId);
        if(email.isPresent() && !email.get().equals(userAccountInformationDTO.getEmail()) && userService.doesEmailExist(userAccountInformationDTO.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        return userService.updateAccountInformation(userId, userAccountInformationDTO)
                ? ResponseEntity.ok(userAccountInformationDTO)
                : ResponseEntity.internalServerError().build();
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long userId, @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return userService.updateUserPassword(userId, updatePasswordDTO)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.notFound().build();
    }
}
