package com.service.userservice.controller;

import com.service.userservice.payload.inc.TutorialPage;
import com.service.userservice.services.DbQueryService;
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
    public ResponseEntity<Void> doesUserHaveTutorialPage(@RequestHeader Long userId, @PathVariable TutorialPage tutorialPage) {
        return dbQueryService.doesUserHaveTutorialPage(userId, tutorialPage)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/{tutorialPage}")
    public ResponseEntity<Void> saveUserTutorialPage(@RequestHeader Long userId, @PathVariable TutorialPage tutorialPage) {
        return ResponseEntity.status(dbQueryService.saveTutorialPage(userId, tutorialPage)).build();
    }
}
