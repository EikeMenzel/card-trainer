package com.service.cardsservice.controller;

import com.service.cardsservice.payload.in.CardDTO;
import com.service.cardsservice.payload.in.DeckNameDTO;
import com.service.cardsservice.payload.in.MailDTO;
import com.service.cardsservice.payload.out.DeckDetailInformationDTO;
import com.service.cardsservice.payload.out.DeckInformationDTO;
import com.service.cardsservice.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/decks")
public class DeckController {
    private final DeckService deckService;
    private final DbQueryService dbQueryService;
    private final AchievementQueryService achievementQueryService;
    private final ExportService exportService;
    private final ImportService importService;
    private final String GATEWAY_PATH;

    public DeckController(DeckService deckService, DbQueryService dbQueryService, AchievementQueryService achievementQueryService, ExportService exportService, ImportService importService, @Value("${gateway.path}") String gatewayPath) {
        this.deckService = deckService;
        this.dbQueryService = dbQueryService;
        this.achievementQueryService = achievementQueryService;
        this.exportService = exportService;
        this.importService = importService;
        this.GATEWAY_PATH = gatewayPath;
    }

    @GetMapping()
    @Operation(summary = "Retrieve All Decks",
            description = "Fetches all decks associated with a specific user.<br><br>" +
                    "<strong>Note:</strong> The user ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Decks successfully retrieved", content = @Content(schema = @Schema(implementation = DeckInformationDTO[].class))),
                    @ApiResponse(responseCode = "204", description = "No decks found"),
                    @ApiResponse(responseCode = "500", description = "Service was not reachable")
            })
    public ResponseEntity<List<DeckInformationDTO>> getAllDecksByUserId(
            @Parameter(description = "User ID of the requester", required = true) @RequestHeader Long userId) {
        if (dbQueryService.saveUserLogin(userId) == HttpStatus.CREATED) // Save User Login
        {
            achievementQueryService.checkDailyLoginAchievement(userId); //Check for daily Login
        }

        List<DeckInformationDTO> deckInformationDTOS = deckService.getAllDeckInformation(userId);

        return deckInformationDTOS.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(deckInformationDTOS);
    }

    @PostMapping()
    @Operation(summary = "Create Deck",
            description = "Creates a new deck for the user.<br><br>" +
                    "<strong>Note:</strong> Deck name should be non-empty and within the specified character limit." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Deck successfully created"),
                    @ApiResponse(responseCode = "400", description = "Bad request, invalid input"),
                    @ApiResponse(responseCode = "500", description = "Service was not reachable")
            })
    public ResponseEntity<Void> createDeck(
            @Parameter(description = "User ID of the creator", required = true) @RequestHeader Long userId,
            @Parameter(description = "Name of the deck which should be created", required = true,
                    content = @Content(schema = @Schema(implementation = DeckNameDTO.class))) @Valid @RequestBody DeckNameDTO deckNameDTO) {

        if (userId < 0 || deckNameDTO.deckName().isEmpty() || deckNameDTO.deckName().length() > 128)
            return ResponseEntity.badRequest().build();

        var httpStatusCode = dbQueryService.saveDeck(userId, new DeckNameDTO(deckNameDTO.deckName()));
        if (httpStatusCode.is2xxSuccessful())
            achievementQueryService.checkDeckAchievements(userId);

        return ResponseEntity.status(httpStatusCode).build();
    }

    @DeleteMapping("/{deckId}")
    @Operation(summary = "Delete Deck",
            description = "Deletes a specified deck for the user.<br><br>" +
                    "<strong>Note:</strong> User ID and deck ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Deck successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Deck not found"),
                    @ApiResponse(responseCode = "500", description = "Service was not reachable")
            })
    public ResponseEntity<Void> deleteDeck(
            @Parameter(description = "User ID of the requester", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID to be deleted", required = true) @PathVariable Long deckId) {
        return ResponseEntity.status(dbQueryService.deleteDeck(userId, deckId)).build();
    }

    @PutMapping("/{deckId}")
    @Operation(summary = "Update Deck",
            description = "Updates the name or other details of a specified deck.<br><br>" +
                    "<strong>Note:</strong> Valid deck ID and user ID are required, along with valid update information." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Deck successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Bad request, invalid input"),
                    @ApiResponse(responseCode = "500", description = "Service was not reachable")
            })
    public ResponseEntity<Void> updateDeck(
            @Parameter(description = "User ID of the requester", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID to be updated", required = true) @PathVariable Long deckId,
            @Parameter(description = "Name of the deck which should be created", required = true,
                    content = @Content(schema = @Schema(implementation = DeckNameDTO.class))) @Valid @RequestBody DeckNameDTO deckNameDTO) {
        return ResponseEntity.status(dbQueryService.updateDeckInformation(userId, deckId, deckNameDTO)).build();
    }

    @GetMapping("/{deckId}")
    @Operation(summary = "Retrieve Deck Details",
            description = "Fetches detailed information of a specific deck for a given user.<br><br>" +
                    "<strong>Note:</strong> Ensure the deck ID and user ID are valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deck details successfully retrieved", content = @Content(schema = @Schema(implementation = DeckDetailInformationDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Deck not found"),
                    @ApiResponse(responseCode = "500", description = "Service was not reachable")
            })
    public ResponseEntity<DeckDetailInformationDTO> getDetailDeckInformation(
            @Parameter(description = "User ID of the requester", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID of the detail to retrieve", required = true) @PathVariable Long deckId) {
        Optional<DeckDetailInformationDTO> deckDetailInformationDTO = deckService.getDetailInformationDeck(userId, deckId);
        return deckDetailInformationDTO
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{deckId}/size")
    @Operation(summary = "Retrieve Deck Size",
            description = "Fetches the size of a specific deck for a given user.<br><br>" +
                    "<strong>Note:</strong> Ensure the deck ID and user ID are valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deck size retrieved", content = @Content(schema = @Schema(implementation = Integer.class))),
                    @ApiResponse(responseCode = "500", description = "Service was not reachable")
            })
    public ResponseEntity<Integer> getDeckSize(
            @Parameter(description = "User ID of the requester", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID for which the size is to be retrieved", required = true) @PathVariable Long deckId) {
        return ResponseEntity.ok(deckService.getDeckSize(userId, deckId));
    }


    @GetMapping("/{deckId}/export")
    @Operation(summary = "Export Deck",
            description = "Exports a specific deck to a downloadable file format.<br><br>" +
                    "<strong>Note:</strong> Valid deck ID and user ID are required for export." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deck successfully exported", content = @Content(mediaType = "application/octet-stream", schema = @Schema(implementation = ByteArrayResource.class))),
                    @ApiResponse(responseCode = "404", description = "Deck not found"),
                    @ApiResponse(responseCode = "500", description = "Service was not reachable")
            })
    public ResponseEntity<ByteArrayResource> exportDeck(
            @Parameter(description = "User ID of the requester", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID to be exported", required = true) @PathVariable Long deckId) throws IOException {
        byte[] zipData = exportService.zipDeck(userId, deckId);

        if (zipData == null || zipData.length == 0)
            return ResponseEntity.notFound().build();

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=card-trainer.zip");

        var resource = new ByteArrayResource(zipData);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(zipData.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/{deckId}/cards")
    @Operation(summary = "Retrieve All Cards in Deck",
            description = "Fetches all cards from a specified deck for a given user.<br><br>" +
                    "<strong>Note:</strong> The deck ID and user ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cards successfully retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardDTO[].class))),
                    @ApiResponse(responseCode = "204", description = "No cards found in the deck"),
                    @ApiResponse(responseCode = "404", description = "Deck not found"),
                    @ApiResponse(responseCode = "500", description = "Service was not reachable")
            })
    public ResponseEntity<List<CardDTO>> getAllCards(
            @Parameter(description = "User ID of the requester", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID from which cards are to be retrieved", required = true) @PathVariable Long deckId) {
        Pair<List<CardDTO>, HttpStatusCode> pair = dbQueryService.getAllCardsByDeckIdAndUserId(userId, deckId);
        if (pair.getRight() == HttpStatus.NOT_FOUND)
            return ResponseEntity.notFound().build();

        if (pair.getRight() == HttpStatus.NO_CONTENT)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(pair.getLeft());
    }

    @PostMapping("/import")
    @Operation(summary = "Import Deck",
            description = "Imports a deck from a provided file.<br><br>" +
                    "<strong>Note:</strong> The file must be in a 'zip' format." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deck successfully imported"),
                    @ApiResponse(responseCode = "422", description = "Unprocessable entity, invalid file format"),
                    @ApiResponse(responseCode = "500", description = "Service was not reachable or internal problems")
            })
    public ResponseEntity<Void> importDeck(
            @Parameter(description = "User ID of the importer", required = true) @RequestHeader Long userId,
            @Parameter(description = "Zip file containing the deck", required = true) @RequestParam("file") MultipartFile multipartFile) {
        if (!Objects.equals(multipartFile.getContentType(), "application/x-zip-compressed"))
            return ResponseEntity.unprocessableEntity().build();

        return ResponseEntity.status(importService.processZipFile(multipartFile, userId)).build();
    }

    @PostMapping("/{deckId}/share")
    @Operation(summary = "Share Deck via Email",
            description = "Sends an email to share a deck with another user.<br><br>" +
                    "<strong>Note:</strong> Deck ID must be valid and belong to the user." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Email sent - no confirmation if it was send or not"),
                    @ApiResponse(responseCode = "404", description = "Deck not found or user does not have access to the deck"),
                    @ApiResponse(responseCode = "500", description = "Service was not reachable")
            })
    public ResponseEntity<Void> sendShareDeckEmail(
            @Parameter(description = "User ID of the sender", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID to be shared", required = true) @PathVariable Long deckId,
            @Parameter(description = "MailDTO, with the email", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MailDTO.class))) @Valid @RequestBody MailDTO mailDTO) {
    if (dbQueryService.existsDeckByUserIdAndDeckId(userId, deckId) == HttpStatus.NOT_FOUND)
            return ResponseEntity.notFound().build();

        return ResponseEntity.status(dbQueryService.sendShareDeckEmail(mailDTO.email(), deckId)).build();
    }

    @GetMapping("/{deckId}/cards-to-learn")
    @Operation(summary = "Count Cards to Learn in Deck",
            description = "Retrieves the count of cards to learn in a specified deck for a given user.<br><br>" +
                    "<strong>Note:</strong> The deck ID and user ID must be valid." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Count retrieved successfully", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Service was not reachable")
            })
    public ResponseEntity<Integer> getCardsToLearnSize(
            @Parameter(description = "User ID of the requester", required = true) @RequestHeader Long userId,
            @Parameter(description = "Deck ID for which to retrieve the learnable cards count", required = true) @PathVariable Long deckId) {
        return ResponseEntity.ok(dbQueryService.getCardsToLearnAmountByDeck(userId, deckId));
    }

    //Needs to be a get-mapping, because you can't execute Javascript code in a e-mail
    @GetMapping("/share/{token}")
    @Operation(summary = "Copy Shared Deck",
            description = "Copies a shared deck using a token.<br><br>" +
                    "<strong>Note:</strong> The token must be valid and corresponds to a shareable deck.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Deck successfully copied"),
                    @ApiResponse(responseCode = "404", description = "Token not found or invalid"),
                    @ApiResponse(responseCode = "309", description = "Permanent redirect to the application"),
                    @ApiResponse(responseCode = "500", description = "Service was not reachable")
            })
    public ResponseEntity<Void> copySharedDeck(@PathVariable @Size(min = 36, max = 50) String token) {
        var httpStatusCode = dbQueryService.shareDeck(token);
        if (httpStatusCode == HttpStatus.CREATED)
            return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).location(URI.create(GATEWAY_PATH + "/")).build();

        return ResponseEntity.status(httpStatusCode).build();
    }
}
