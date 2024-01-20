package com.service.databaseservice.controller;

import com.service.databaseservice.model.tutorial.TutorialPage;
import com.service.databaseservice.services.TutorialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/db/users")
public class TutorialController {
    private final TutorialService tutorialService;

    public TutorialController(TutorialService tutorialService) {
        this.tutorialService = tutorialService;
    }

    @GetMapping("/{userId}/tutorials/{tutorialPage}")
    public ResponseEntity<Void> doesUserHaveTutorialPage(@PathVariable Long userId, @PathVariable TutorialPage tutorialPage) {
        return tutorialService.doesUserHasTutorialPage(tutorialPage, userId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/{userId}/tutorials/{tutorialPage}")
    public ResponseEntity<Void> saveUserTutorialPage(@PathVariable Long userId, @PathVariable TutorialPage tutorialPage) {
        return tutorialService.saveTutorialPage(tutorialPage, userId)
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.internalServerError().build();
    }
}
