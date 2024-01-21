package com.service.databaseservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.User;
import com.service.databaseservice.payload.inc.DeckNameDTO;
import com.service.databaseservice.payload.out.DeckDTO;
import com.service.databaseservice.payload.out.export.CardExportDTO;
import com.service.databaseservice.payload.out.export.ExportDTO;
import com.service.databaseservice.payload.out.export.MultipleChoiceCardDTO;
import com.service.databaseservice.payload.out.export.TextAnswerDTO;
import com.service.databaseservice.payload.savecard.ChoiceAnswerDTO;
import com.service.databaseservice.payload.savecard.TextAnswerCardDTO;
import com.service.databaseservice.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("api/v1/db")
public class DeckController {
    private final DeckService deckService;
    private final CardService cardService;
    private final RepetitionService repetitionService;
    private final ExportService exportService;
    private final ObjectMapper objectMapper;
    private final UserTokenService userTokenService;
    private final ImageService imageService;

    public DeckController(DeckService deckService, CardService cardService, RepetitionService repetitionService, ExportService exportService, ObjectMapper objectMapper, UserTokenService userTokenService, ImageService imageService) {
        this.deckService = deckService;
        this.cardService = cardService;
        this.repetitionService = repetitionService;
        this.exportService = exportService;
        this.objectMapper = objectMapper;
        this.userTokenService = userTokenService;
        this.imageService = imageService;
    }

    @GetMapping("/users/{userId}/decks")
    @Operation(summary = "Retrieve All Decks by User",
            description = "Fetches all decks associated with the specified user ID.<br><br>" +
                    "<strong>Note:</strong> User ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Decks successfully retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeckDTO[].class))),
                    @ApiResponse(responseCode = "204", description = "No decks found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<List<DeckDTO>> getAllDecksByUserId(
            @Parameter(description = "User ID for whom decks are retrieved", required = true) @PathVariable Long userId) {
        List<DeckDTO> deckDTOS = deckService.getAllDecksByUserId(userId);
        return deckDTOS.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(deckDTOS);
    }

    @GetMapping("/users/{userId}/decks/{deckId}")
    @Operation(summary = "Retrieve Deck by ID and User ID",
            description = "Fetches details of a specific deck for the given user ID and deck ID.<br><br>" +
                    "<strong>Note:</strong> Both user ID and deck ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deck details successfully retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeckDTO.class))),
                    @ApiResponse(responseCode = "204", description = "Deck or user not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<DeckDTO> getDeckByIdAndUserId(
            @Parameter(description = "User ID of the deck owner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID to retrieve", required = true) @PathVariable Long deckId) {
        Optional<DeckDTO> deckDTO = deckService.getDeckByIdAndUserId(userId, deckId);
        return deckDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/users/{userId}/decks/count")
    @Operation(summary = "Count User's Decks",
            description = "Counts the number of decks associated with the specified user ID.<br><br>" +
                    "<strong>Note:</strong> User ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deck count successfully retrieved", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Integer> getDeckCount(
            @Parameter(description = "User ID for whom deck count is calculated", required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(deckService.getDeckCountByUserId(userId));
    }

    @GetMapping("/decks/{deckId}/name")
    @Operation(summary = "Retrieve Deck Name",
            description = "Fetches the name of a specific deck by its ID.<br><br>" +
                    "<strong>Note:</strong> Deck ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deck name successfully retrieved", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "204", description = "Deck not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<String> getDeckName(
            @Parameter(description = "Deck ID to retrieve the name for", required = true) @PathVariable Long deckId) {
        Optional<String> deckDTO = deckService.getDeckNameById(deckId);
        return deckDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/users/{userId}/decks/{deckId}/cards/count")
    @Operation(summary = "Get Card Count in Deck",
            description = "Counts the number of cards in a specified deck for a given user.<br><br>" +
                    "<strong>Note:</strong> The deck ID and user ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Card count successfully retrieved"),
                    @ApiResponse(responseCode = "404", description = "Deck or user not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Integer> getAmountOfCardsInDeck(
            @Parameter(description = "User ID of the deck owner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID to count cards from", required = true) @PathVariable Long deckId) {
        return deckService.existsByDeckIdAndUserId(deckId, userId)
                ? ResponseEntity.ok(cardService.getCardAmountFromDeckId(deckId))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/users/{userId}/decks/{deckId}/cards-to-learn/count")
    @Operation(summary = "Get Amount of Cards to Learn",
            description = "Calculates the number of cards that need to be learned in a specified deck for a given user.<br><br>" +
                    "<strong>Note:</strong> The deck ID and user ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cards to learn count successfully retrieved"),
                    @ApiResponse(responseCode = "404", description = "Deck or user not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Integer> getCardsToLearnAmount(
            @Parameter(description = "User ID of the learner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID to calculate the learning cards from", required = true) @PathVariable Long deckId) {
        Integer count = cardService.getCardsByDeckId(deckId).stream()
                .mapToInt(card -> repetitionService.getRepetitionByCardId(card.getId())
                        .filter(repetition -> LocalDateTime.now().isAfter(repetition.getNextLearnTimestamp().toLocalDateTime()))
                        .map(repetition -> 1)
                        .orElse(0))
                .sum();

        return deckService.existsByDeckIdAndUserId(deckId, userId)
                ? ResponseEntity.ok(count)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/users/{userId}/decks/{deckId}/learn-state")
    @Operation(summary = "Count Card Learning Values",
            description = "Counts the learning values of the cards in a specified deck for a given user.<br><br>" +
                    "<strong>Note:</strong> The deck ID and user ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Card learning values count successfully retrieved"),
                    @ApiResponse(responseCode = "404", description = "Deck or user not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")

            })
    public ResponseEntity<List<Integer>> countCardValues(
            @Parameter(description = "User ID of the learner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID to count learning values from", required = true) @PathVariable Long deckId) {
        if (!deckService.existsByDeckIdAndUserId(deckId, userId))
            return ResponseEntity.notFound().build();

        Map<Integer, Long> qualityCount = cardService.getCardsByDeckId(deckId)
                .stream()
                .map(card -> repetitionService.getRepetitionByCardId(card.getId()))
                .filter(Optional::isPresent)
                .map(optionalRepetition -> optionalRepetition.get().getQuality())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return ResponseEntity.ok(
                IntStream.rangeClosed(-1, 5)
                        .mapToObj(i -> qualityCount.getOrDefault(i, 0L).intValue())
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("users/{userId}/decks/")
    @Operation(summary = "Create Deck",
            description = "Creates a new deck for the specified user with the provided deck name.<br><br>" +
                    "<strong>Note:</strong> Requires a valid user ID and deck name." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Deck successfully created"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> createDeck(
            @Parameter(description = "User ID who is creating the deck", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck name", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeckNameDTO.class))) @RequestBody DeckNameDTO deckNameDTO) {
        return deckService.createDeck(userId, deckNameDTO)
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/users/{userId}/decks/{deckId}")
    @Operation(summary = "Delete Deck",
            description = "Deletes a specific deck for the given user ID.<br><br>" +
                    "<strong>Note:</strong> Both user ID and deck ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Deck successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Deck or user not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Void> deleteDeck(
            @Parameter(description = "User ID of the deck owner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID to be deleted", required = true) @PathVariable Long deckId) {
        return deckService.deleteDeck(userId, deckId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{userId}/decks/{deckId}")
    @Operation(summary = "Update Deck Name",
            description = "Updates the name of a specific deck for the given user ID.<br><br>" +
                    "<strong>Note:</strong> Requires valid user ID, deck ID, and a new deck name." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Deck name successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Deck or user not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Void> updateDeckName(
            @Parameter(description = "User ID of the deck owner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID whose name is to be updated", required = true) @PathVariable Long deckId,
            @Parameter(description = "Deck name", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeckNameDTO.class))) @RequestBody DeckNameDTO deckNameDTO) {
        return deckService.updateDeckInformation(userId, deckId, deckNameDTO)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/users/{userId}/decks/{deckId}/cards/export")
    @Operation(summary = "Export Cards from Deck",
            description = "Exports the cards from a specified deck for a given user.<br><br>" +
                    "<strong>Note:</strong> The deck ID and user ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cards successfully prepared for export", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExportDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Deck or user not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<ExportDTO> getCardsForExport(
            @Parameter(description = "User ID of the deck owner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID from which cards are exported", required = true) @PathVariable Long deckId) {
        Optional<String> deckName = deckService.getDeckNameByIdAndUserId(userId, deckId);
        Optional<List<CardExportDTO>> cardExportDTOS = exportService.getDeckForExport(userId, deckId);
        if (cardExportDTOS.isEmpty() || deckName.isEmpty())
            return ResponseEntity.notFound().build();

        if (cardExportDTOS.get().isEmpty())
            return ResponseEntity.ok(new ExportDTO(deckName.get(), List.of()));

        return ResponseEntity.ok(new ExportDTO(deckName.get(), cardExportDTOS.get()));
    }

    @PostMapping("/users/{userId}/decks/import")
    @Operation(summary = "Import Deck",
            description = "Imports a deck for the given user ID from the provided export data.<br><br>" +
                    "<strong>Note:</strong> Requires a valid user ID and export data." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Deck successfully imported"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> importDeck(
            @Parameter(description = "User ID who is importing the deck", required = true) @PathVariable Long userId,
            @Parameter(description = "ExportDTO, which comes from the export function", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExportDTO.class))) @RequestBody ExportDTO exportDTO) {
        if (!deckService.createDeck(userId, new DeckNameDTO(exportDTO.deckName())))
            return ResponseEntity.internalServerError().build();

        Optional<Deck> deck = deckService.findTopByOwnerIdAndNameOrderByIdDesc(userId, exportDTO.deckName());
        if (deck.isEmpty())
            return ResponseEntity.internalServerError().build();

        exportDTO.cardExportDTOList().forEach(dto -> {
            if (dto instanceof TextAnswerDTO textAnswerDTO) {
                var cardDTO = new com.service.databaseservice.payload.savecard.CardDTO(textAnswerDTO.getCardDTO().question(), imageService.saveImage(userId, textAnswerDTO.getCardDTO().image()).orElse(null));
                var jsonNode = objectMapper.valueToTree(new TextAnswerCardDTO(cardDTO, textAnswerDTO.getTextAnswer(), imageService.saveImage(userId, textAnswerDTO.getImage()).orElse(null)));
                cardService.saveCard(jsonNode, 1L, deck.get().getId());
            } else if (dto instanceof MultipleChoiceCardDTO multipleChoiceCardDTO) {
                var cardDTO = new com.service.databaseservice.payload.savecard.CardDTO(multipleChoiceCardDTO.getCardDTO().question(), imageService.saveImage(userId, multipleChoiceCardDTO.getCardDTO().image()).orElse(null));
                List<ChoiceAnswerDTO> choiceAnswerDTOList = multipleChoiceCardDTO.getChoiceAnswers()
                        .stream()
                        .map(choiceAnswerDTO -> new ChoiceAnswerDTO(choiceAnswerDTO.answer(), choiceAnswerDTO.getIsRightAnswer(), imageService.saveImage(userId, choiceAnswerDTO.image()).orElse(null)))
                        .toList();

                var jsonNode = objectMapper.valueToTree(new com.service.databaseservice.payload.savecard.MultipleChoiceCardDTO(cardDTO, choiceAnswerDTOList));
                cardService.saveCard(jsonNode, userId, deck.get().getId());
            }
        });
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/users/{userId}/decks/{deckId}/exists")
    @Operation(summary = "Check Deck Existence",
            description = "Checks if a specific deck exists for a given user.<br><br>" +
                    "<strong>Note:</strong> Both user ID and deck ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Deck exists"),
                    @ApiResponse(responseCode = "404", description = "Deck not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<Void> existsDeckByUserIdAndDeckId(
            @Parameter(description = "User ID of the deck owner", required = true) @PathVariable Long userId,
            @Parameter(description = "Deck ID to check existence for", required = true) @PathVariable Long deckId) {
        return deckService.existsByDeckIdAndUserId(deckId, userId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/decks/share/{token}")
    @Operation(summary = "Clone Shared Deck",
            description = "Clones a shared deck using a token.<br><br>" +
                    "<strong>Note:</strong> Requires a valid sharing token." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Deck successfully cloned"),
                    @ApiResponse(responseCode = "404", description = "Token invalid or deck not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> cloneSharedDeck(
            @Parameter(description = "Token for shared deck", required = true) @PathVariable String token) {
        Optional<User> userOptional = userTokenService.getUserByUserToken(token);
        if (userOptional.isEmpty() || !userTokenService.isUserTokenValid(token) || !userTokenService.areTokenTypesIdentical(token, "SHARE_DECK"))
            return ResponseEntity.notFound().build();

        Optional<Long> deckId = userTokenService.getDeckIdByUserToken(token);

        if (deckId.isEmpty())
            return ResponseEntity.notFound().build();

        boolean wasCloned = deckService.cloneSharedDeck(userOptional.get().getId(), deckId.get());

        if (wasCloned && userTokenService.deleteToken(userOptional.get().getId(), token))
            return ResponseEntity.status(HttpStatus.CREATED).build();
        else
            return ResponseEntity.internalServerError().build();
    }
}
