package com.service.cardsservice.controller;

import com.service.cardsservice.payload.in.DeckNameDTO;
import com.service.cardsservice.payload.out.DeckDetailInformationDTO;
import com.service.cardsservice.payload.out.DeckInformationDTO;
import com.service.cardsservice.services.DbQueryService;
import com.service.cardsservice.services.DeckService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/decks")
public class DeckController {
    private final DeckService deckService;
    private final DbQueryService dbQueryService;

    public DeckController(DeckService deckService, DbQueryService dbQueryService) {
        this.deckService = deckService;
        this.dbQueryService = dbQueryService;
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
}
