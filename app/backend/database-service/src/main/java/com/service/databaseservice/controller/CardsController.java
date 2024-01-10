package com.service.databaseservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.service.databaseservice.payload.out.CardDTO;
import com.service.databaseservice.services.CardService;
import com.service.databaseservice.services.DeckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/db")
public class CardsController {
    private final DeckService deckService;
    private final CardService cardService;

    public CardsController(DeckService deckService, CardService cardService) {
        this.deckService = deckService;
        this.cardService = cardService;
    }

    @GetMapping("/users/{userId}/decks/{deckId}/cards")
    @Operation(summary = "Retrieve All Cards by Deck",
            description = "Fetches all cards from a specified deck for a given user.<br><br>" +
                    "<strong>Note:</strong> The deck ID and user ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cards successfully retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardDTO[].class))),
                    @ApiResponse(responseCode = "204", description = "No cards in the deck"),
                    @ApiResponse(responseCode = "404", description = "Deck or user not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<List<CardDTO>> getAllCardsByDeckIdAndUserId(
            @Parameter(description = "User ID for whom the cards are being retrieved", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID from which cards are to be retrieved", required = true) @PathVariable Long deckId) {
        if (!deckService.existsByDeckIdAndUserId(deckId, userId))
            return ResponseEntity.notFound().build();

        List<CardDTO> cardDTOList = cardService.getCardsFromDeckId(deckId);
        return cardDTOList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(cardDTOList);
    }

    @DeleteMapping("/users/{userId}/decks/{deckId}/cards/{cardId}")
    @Operation(summary = "Delete Card",
            description = "Deletes a card from the specified deck of a user.<br><br>" +
                    "<strong>Note:</strong> The card, deck, and user IDs must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Card successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Card or deck or user not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Void> deleteCard(
            @Parameter(description = "User ID of the owner of the deck", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID from which the card is to be deleted", required = true) @PathVariable Long deckId,
            @Parameter(description = "Card ID of the card to be deleted", required = true) @PathVariable Long cardId) {
        if (!deckService.existsByDeckIdAndUserId(deckId, userId))
            return ResponseEntity.notFound().build();

        return cardService.deleteCard(cardId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/users/{userId}/decks/{deckId}/cards")
    @Operation(summary = "Save Card",
            description = "Saves a new card to the specified deck of a user.<br><br>" +
                    "<strong>Note:</strong> The deck ID and user ID must be valid, and the card information must be correctly formatted." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Card successfully created"),
                    @ApiResponse(responseCode = "404", description = "deck or user not found or does not belong to user"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> saveCard(
            @Parameter(description = "User ID of the deck owner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID to which the new card is to be saved", required = true) @PathVariable Long deckId,
            @Parameter(description = "Card data in JSON format", required = true) @RequestBody JsonNode cardNode) {
        if (!deckService.existsByDeckIdAndUserId(deckId, userId))
            return ResponseEntity.notFound().build();

        return cardService.saveCard(cardNode, userId, deckId)
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.internalServerError().build();
    }

    @PutMapping("/users/{userId}/decks/{deckId}/cards/{cardId}")
    @Operation(summary = "Update Card",
            description = "Updates an existing card in a specified deck for a user.<br><br>" +
                    "<strong>Note:</strong> The card, deck, and user IDs must be valid, and the card information must be correctly formatted." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Card successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Card or deck or user not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> updateCard(
            @Parameter(description = "User ID of the deck owner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID to which the card belongs", required = true) @PathVariable Long deckId,
            @Parameter(description = "Card ID of the card to be updated", required = true) @PathVariable Long cardId,
            @Parameter(description = "Updated card data in JSON format", required = true) @RequestBody JsonNode cardNode) {
        if (!cardService.doesCardBelongToOwnerAndDeck(userId, deckId, cardId)) {
            return ResponseEntity.notFound().build();
        }

        return cardService.updateCard(cardNode, userId, cardId)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.internalServerError().build();
    }

    @GetMapping("/users/{userId}/decks/{deckId}/cards/{cardId}")
    @Operation(summary = "Retrieve Card Details",
            description = "Fetches detailed information about a specific card from a deck of a user.<br><br>" +
                    "<strong>Note:</strong> The card, deck, and user IDs must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Card details successfully retrieved", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Card or deck not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Object> getCardDetails(
            @Parameter(description = "User ID of the deck owner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID from which card details are to be retrieved", required = true) @PathVariable Long deckId,
            @Parameter(description = "Card ID of the card whose details are to be retrieved", required = true) @PathVariable Long cardId) {
        if (!deckService.existsByDeckIdAndUserId(deckId, userId))
            return ResponseEntity.notFound().build();

        Object obj = cardService.getCardDetails(cardId);
        return obj == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(obj);
    }
}
