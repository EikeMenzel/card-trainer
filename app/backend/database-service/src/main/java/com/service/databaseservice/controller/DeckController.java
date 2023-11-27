package com.service.databaseservice.controller;

import com.service.databaseservice.payload.inc.DeckNameDTO;
import com.service.databaseservice.payload.out.DeckDTO;
import com.service.databaseservice.services.CardService;
import com.service.databaseservice.services.DeckService;
import com.service.databaseservice.services.RepetitionService;
import jakarta.validation.Valid;
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

    public DeckController(DeckService deckService, CardService cardService, RepetitionService repetitionService) {
        this.deckService = deckService;
        this.cardService = cardService;
        this.repetitionService = repetitionService;
    }

    @GetMapping("/users/{userId}/decks")
    public ResponseEntity<List<DeckDTO>> getAllDecksByUserId(@PathVariable Long userId) {
        List<DeckDTO> deckDTOS = deckService.getAllDecksByUserId(userId);
        return deckDTOS.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(deckDTOS);
    }

    @GetMapping("/users/{userId}/decks/{deckId}")
    public ResponseEntity<DeckDTO> getDeckByIdAndUserId(@PathVariable Long userId, @PathVariable Long deckId) {
        Optional<DeckDTO> deckDTO = deckService.getDeckByIdAndUserId(userId, deckId);
        return deckDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }


    @GetMapping("/users/{userId}/decks/{deckId}/cards/count")
    public ResponseEntity<Integer> getAmountOfCardsInDeck(@PathVariable Long userId, @PathVariable Long deckId) {
        return deckService.existsByDeckIdAndUserId(deckId, userId)
                ? ResponseEntity.ok(cardService.getCardAmountFromDeckId(deckId))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/users/{userId}/decks/{deckId}/cards-to-learn/count")
    public ResponseEntity<Integer> getCardsToLearnAmount(@PathVariable Long userId, @PathVariable Long deckId) {
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
    public ResponseEntity<List<Integer>> countCardValues(@PathVariable Long userId, @PathVariable Long deckId) {
        if(!deckService.existsByDeckIdAndUserId(deckId, userId))
            return ResponseEntity.notFound().build();

        Map<Integer, Long> qualityCount = cardService.getCardsByDeckId(deckId)
                .stream()
                .map(card -> repetitionService.getRepetitionByCardId(card.getId()))
                .filter(Optional::isPresent)
                .map(optionalRepetition -> optionalRepetition.get().getQuality())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return ResponseEntity.ok(
                IntStream.rangeClosed(0, 5)
                        .mapToObj(i -> qualityCount.getOrDefault(i, 0L).intValue())
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("users/{userId}/decks/")
    public ResponseEntity<?> createDeck(@PathVariable Long userId, @Valid @RequestBody DeckNameDTO deckNameDTO) {
        return deckService.createDeck(userId, deckNameDTO)
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/users/{userId}/decks/{deckId}")
    public ResponseEntity<?> deleteDeck(@PathVariable Long userId, @PathVariable Long deckId) {
        return deckService.deleteDeck(userId, deckId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{userId}/decks/{deckId}")
    public ResponseEntity<?> updateDeckName(@PathVariable Long userId, @PathVariable Long deckId, @Valid @RequestBody DeckNameDTO deckNameDTO) {
        return deckService.updateDeckInformation(userId, deckId, deckNameDTO)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
