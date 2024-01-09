package com.service.cardsservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.cardsservice.services.CardsService;
import com.service.cardsservice.services.DbQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "Save Card to Deck",
            description = "Saves a new card to the specified deck.<br><br>" +
                    "<strong>Note:</strong> The card data should be in a valid JSON format." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Card successfully saved"),
                    @ApiResponse(responseCode = "404", description = "User or deck was not found"),
                    @ApiResponse(responseCode = "422", description = "Unprocessable entity, invalid card data format"),
                    @ApiResponse(responseCode = "500", description = "Some internal error occurred")
            })
    public ResponseEntity<Void> saveCard(
            @Parameter(description = "User ID of the card owner", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID to which the card will be saved", required = true) @PathVariable Long deckId,
            @Parameter(description = "Card data in JSON format", required = true) @RequestBody String cardData) {

        JsonNode cardNode;
        try {
            cardNode = objectMapper.readTree(cardData);
            return ResponseEntity.status(cardsService.saveCard(cardNode, userId, deckId)).build();
        } catch (JsonProcessingException e) {
            logger.debug(e.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PutMapping(value = "/decks/{deckId}/cards/{cardId}")
    @Operation(summary = "Update Card in Deck",
            description = "Updates an existing card in the specified deck.<br><br>" +
                    "<strong>Note:</strong> Requires valid JSON format for card data." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Card successfully updated"),
                    @ApiResponse(responseCode = "404", description = "User or deck was not found"),
                    @ApiResponse(responseCode = "422", description = "Unprocessable entity, invalid card data format"),
                    @ApiResponse(responseCode = "500", description = "Some internal error occurred")
            })
    public ResponseEntity<Void> updateCard(
            @Parameter(description = "User ID of the card owner", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID where the card is located", required = true) @PathVariable Long deckId,
            @Parameter(description = "ID of the card to be updated", required = true) @PathVariable Long cardId,
            @Parameter(description = "Updated card data in JSON format", required = true) @RequestBody String cardData) {

        JsonNode cardNode;
        try {
            cardNode = objectMapper.readTree(cardData);
            return ResponseEntity.status(cardsService.updateCard(cardNode, userId, deckId, cardId)).build();
        } catch (JsonProcessingException e) {
            logger.debug(e.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping("/decks/{deckId}/cards/{cardId}")
    @Operation(summary = "Retrieve Card Details",
            description = "Fetches detailed information of a specific card from a deck.<br><br>" +
                    "<strong>Note:</strong> Ensure the card ID is valid and exists in the specified deck." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of card details", content = @Content(schema = @Schema(implementation = Object.class))),
                    @ApiResponse(responseCode = "404", description = "Card not found"),
                    @ApiResponse(responseCode = "500", description = "Service was not available")
            })
    public ResponseEntity<Object> getDetailCardInformation(
            @Parameter(description = "User ID of the card owner", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID from which to retrieve the card", required = true) @PathVariable Long deckId,
            @Parameter(description = "ID of the card to retrieve", required = true) @PathVariable Long cardId) {

        return dbQueryService.getCardDetails(userId, deckId, cardId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/decks/{deckId}/cards/{cardId}")
    @Operation(summary = "Delete Card from Deck",
            description = "Deletes a card from the specified deck.<br><br>" +
                    "<strong>Note:</strong> Card ID and Deck ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Card successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Card or Deck or User not found"),
                    @ApiResponse(responseCode = "500", description = "Service was not available")
            })
    public ResponseEntity<Void> deleteCard(
            @Parameter(description = "User ID of the card owner", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID from which the card will be deleted", required = true) @PathVariable Long deckId,
            @Parameter(description = "ID of the card to be deleted", required = true) @PathVariable Long cardId) {
        return ResponseEntity.status(dbQueryService.deleteCard(userId, deckId, cardId)).build();
    }
}
