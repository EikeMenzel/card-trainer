package com.service.cardsservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.service.cardsservice.payload.out.updatecards.CardDTO;
import com.service.cardsservice.payload.out.updatecards.ImageDTO;
import com.service.cardsservice.payload.out.updatecards.OperationDTO;
import com.service.cardsservice.payload.out.updatecards.TextAnswerCardDTO;
import com.service.cardsservice.services.CardsService;
import com.service.cardsservice.services.DbQueryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("api/v1")
public class CardsController {
    private final CardsService cardsService;
    private final DbQueryService dbQueryService;

    public CardsController(CardsService cardsService, DbQueryService dbQueryService) {
        this.cardsService = cardsService;
        this.dbQueryService = dbQueryService;
    }

    @PostMapping(value = "/decks/{deckId}/cards", consumes = "multipart/form-data")
    public ResponseEntity<?> saveCard(@RequestHeader Long userId, @PathVariable Long deckId, @Valid @RequestPart JsonNode cardNode, @RequestPart(value = "images", required = false) MultipartFile[] images) {
        return ResponseEntity.status(cardsService.saveCard(cardNode, userId, deckId, images)).build();
    }

    @PutMapping(value = "/decks/{deckId}/cards/{cardId}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateCard(@RequestHeader Long userId, @PathVariable Long deckId, @PathVariable Long cardId, @Valid @RequestPart JsonNode cardNode, @RequestPart(value = "images", required = false) MultipartFile[] images) {
        //return ResponseEntity.ok(new TextAnswerCardDTO(1, new CardDTO(1, "NewAnswer", null), "New Question lol", new ImageDTO(1L, "BackendEntwurf.drawio(5).png", OperationDTO.UPDATE)));
        return ResponseEntity.status(cardsService.updateCard(cardNode, userId, deckId, cardId, images)).build();

        //return ResponseEntity.status(555).build();
    }

    @GetMapping("/decks/{deckId}/cards/{cardId}")
    public ResponseEntity<?> getDetailCardInformation(@RequestHeader Long userId, @PathVariable Long deckId, @PathVariable Long cardId) {
        return dbQueryService.getCardDetails(userId, deckId, cardId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/decks/{deckId}/cards/{cardId}")
    public ResponseEntity<?> deleteCard(@RequestHeader Long userId, @PathVariable Long deckId, @PathVariable Long cardId) {
        return ResponseEntity.status(dbQueryService.deleteCard(userId, deckId, cardId)).build();
    }
}
