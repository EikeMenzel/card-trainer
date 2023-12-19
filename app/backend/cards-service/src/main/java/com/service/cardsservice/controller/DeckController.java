package com.service.cardsservice.controller;

import com.service.cardsservice.payload.in.CardDTO;
import com.service.cardsservice.payload.in.DeckNameDTO;
import com.service.cardsservice.payload.in.MailDTO;
import com.service.cardsservice.payload.out.DeckDetailInformationDTO;
import com.service.cardsservice.payload.out.DeckInformationDTO;
import com.service.cardsservice.services.DbQueryService;
import com.service.cardsservice.services.DeckService;
import com.service.cardsservice.services.ExportService;
import com.service.cardsservice.services.ImportService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final ExportService exportService;
    private final ImportService importService;
    private final Logger logger = LoggerFactory.getLogger(DeckController.class);
    private final String GATEWAY_PATH;

    public DeckController(DeckService deckService, DbQueryService dbQueryService, ExportService exportService, ImportService importService,  @Value("${gateway.path}") String gatewayPath) {
        this.deckService = deckService;
        this.dbQueryService = dbQueryService;
        this.exportService = exportService;
        this.importService = importService;
        this.GATEWAY_PATH = gatewayPath;
    }

    @GetMapping("")
    public ResponseEntity<List<DeckInformationDTO>> getAllDecksByUserId(@RequestHeader Long userId) {
        List<DeckInformationDTO> deckInformationDTOS = deckService.getAllDeckInformation(userId);
        return deckInformationDTOS.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(deckInformationDTOS);
    }

    @PostMapping("")
    public ResponseEntity<?> createDeck(@RequestHeader Long userId, @Valid @RequestBody DeckNameDTO deckNameDTO) {
        if(userId < 0 || deckNameDTO.deckName().isEmpty() || deckNameDTO.deckName().length() > 128)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.status(dbQueryService.saveDeck(userId, new DeckNameDTO(deckNameDTO.deckName()))).build();
    }

    @DeleteMapping("/{deckId}")
    public ResponseEntity<?> deleteDeck(@RequestHeader Long userId, @PathVariable Long deckId) {
        return ResponseEntity.status(dbQueryService.deleteDeck(userId, deckId)).build();
    }

    @PutMapping("/{deckId}")
    public ResponseEntity<?> updateDeck(@RequestHeader Long userId, @PathVariable Long deckId, @Valid @RequestBody DeckNameDTO deckNameDTO) {
        return ResponseEntity.status(dbQueryService.updateDeckInformation(userId, deckId, deckNameDTO)).build();
    }

    @GetMapping("/{deckId}")
    public ResponseEntity<DeckDetailInformationDTO> getDetailDeckInformation(@RequestHeader Long userId, @PathVariable Long deckId) {
        Optional<DeckDetailInformationDTO> deckDetailInformationDTO = deckService.getDetailInformationDeck(userId, deckId);
        return deckDetailInformationDTO
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{deckId}/export")
    public ResponseEntity<ByteArrayResource> exportDeck(@RequestHeader Long userId, @PathVariable Long deckId) throws IOException {
        byte[] zipData = exportService.zipDeck(userId, deckId);

        if(zipData == null || zipData.length == 0)
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
    public ResponseEntity<List<CardDTO>> getAllCards(@RequestHeader Long userId, @PathVariable Long deckId) {
        Pair<List<CardDTO>, HttpStatusCode> pair = dbQueryService.getAllCardsByDeckIdAndUserId(userId, deckId);
        if(pair.getRight() == HttpStatus.NOT_FOUND)
            return ResponseEntity.notFound().build();

        if(pair.getRight() == HttpStatus.NO_CONTENT)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(pair.getLeft());
    }

    @PostMapping("/import")
    public ResponseEntity<?> importDeck(@RequestHeader Long userId, @RequestParam("file") MultipartFile multipartFile) {
        if(!Objects.equals(multipartFile.getContentType(), "application/x-zip-compressed"))
            return ResponseEntity.unprocessableEntity().build();

        return ResponseEntity.status(importService.processZipFile(multipartFile, userId)).build();
    }

    @PostMapping("/{deckId}/share")
    public ResponseEntity<?> sendShareDeckEmail(@RequestHeader Long userId, @PathVariable Long deckId, @Valid @RequestBody MailDTO mailDTO) {
        if(dbQueryService.existsDeckByUserIdAndDeckId(userId, deckId) == HttpStatus.NOT_FOUND)
            return ResponseEntity.notFound().build();

        return ResponseEntity.status(dbQueryService.sendShareDeckEmail(mailDTO.email(), deckId)).build();
    }

    @GetMapping("/{deckId}/cards-to-learn")
    public ResponseEntity<?> getCardsToLearnSize(@RequestHeader Long userId, @PathVariable Long deckId) {
        return ResponseEntity.ok(dbQueryService.getCardsToLearnAmountByDeck(userId, deckId));
    }

    //Needs to be a get-mapping, because you can't execute Javascript code in a e-mail
    @GetMapping("/share/{token}")
    public ResponseEntity<?> copySharedDeck(@PathVariable @Size(min = 36, max = 50) String token) {
        var httpStatusCode = dbQueryService.shareDeck(token);
        if(httpStatusCode == HttpStatus.CREATED)
            return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).location(URI.create(GATEWAY_PATH + "/")).build();

        return ResponseEntity.status(httpStatusCode).build();
    }
}
