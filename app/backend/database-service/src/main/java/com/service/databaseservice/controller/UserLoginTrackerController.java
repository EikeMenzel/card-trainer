package com.service.databaseservice.controller;

import com.service.databaseservice.services.UserLoginTrackerService;
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
    public ResponseEntity<Void> saveUserLogin(@PathVariable Long userId) {
        return ResponseEntity.status(userLoginTrackerService.saveUserLogin(userId)).build();
    }

    @GetMapping("users/{userId}/user-login-tracker")
    public ResponseEntity<Boolean> wasUserDailyLoggedIn(@PathVariable Long userId) {
        return ResponseEntity.ok(userLoginTrackerService.wasUserLoggedInToday(userId));
    }
}
