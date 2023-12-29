package com.service.cardsservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.cardsservice.payload.out.updatecards.CardDTO;
import com.service.cardsservice.payload.out.updatecards.ImageDTO;
import com.service.cardsservice.payload.out.updatecards.OperationDTO;
import com.service.cardsservice.payload.out.updatecards.TextAnswerCardDTO;
import com.service.cardsservice.services.CardsService;
import com.service.cardsservice.services.DbQueryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("api/v1")
public class CardsController {
    private final CardsService cardsService;
    private final DbQueryService dbQueryService;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(CardsController.class);
    public CardsController(CardsService cardsService, DbQueryService dbQueryService, ObjectMapper objectMapper) {
        this.cardsService = cardsService;
        this.dbQueryService = dbQueryService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/decks/{deckId}/cards")
    public ResponseEntity<?> saveCard(@RequestHeader Long userId, @PathVariable Long deckId, @RequestBody() String cardData) {
        JsonNode cardNode;
        try {
            cardNode = objectMapper.readTree(cardData);
            return ResponseEntity.status(cardsService.saveCard(cardNode, userId, deckId)).build();
        } catch (JsonProcessingException e) {
            logger.debug(e.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PutMapping(value = "/decks/{deckId}/cards/{cardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCard(@RequestHeader Long userId, @PathVariable Long deckId, @PathVariable Long cardId, @RequestPart(value = "cardNode") String cardNodeString, @RequestPart(value = "images", required = false) MultipartFile[] images) {
        JsonNode cardNode;
        try {
            cardNode = objectMapper.readTree(cardNodeString);
            return ResponseEntity.status(cardsService.updateCard(cardNode, userId, deckId, cardId, images)).build();
        } catch (JsonProcessingException e) {
            logger.debug(e.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }
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
