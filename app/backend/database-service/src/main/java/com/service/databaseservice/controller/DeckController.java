package com.service.databaseservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.User;
import com.service.databaseservice.payload.inc.DeckNameDTO;
import com.service.databaseservice.payload.out.DeckDTO;
import com.service.databaseservice.payload.import_function.CardDTO;
import com.service.databaseservice.payload.import_function.ChoiceAnswerDTO;
import com.service.databaseservice.payload.import_function.TextAnswerCardDTO;
import com.service.databaseservice.payload.out.export.CardExportDTO;
import com.service.databaseservice.payload.out.export.ExportDTO;
import com.service.databaseservice.payload.out.export.MultipleChoiceCardDTO;
import com.service.databaseservice.payload.out.export.TextAnswerDTO;
import com.service.databaseservice.services.*;
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
    private final ExportService exportService;
    private final ObjectMapper objectMapper;
    private final UserTokenService userTokenService;

    public DeckController(DeckService deckService, CardService cardService, RepetitionService repetitionService, ExportService exportService, ObjectMapper objectMapper, UserService userService, UserTokenService userTokenService) {
        this.deckService = deckService;
        this.cardService = cardService;
        this.repetitionService = repetitionService;
        this.exportService = exportService;
        this.objectMapper = objectMapper;
        this.userTokenService = userTokenService;
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

    @GetMapping("/decks/{deckId}/name")
    public ResponseEntity<String> getDeckName(@PathVariable Long deckId) {
        Optional<String> deckDTO = deckService.getDeckNameById(deckId);
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

    @GetMapping("/users/{userId}/decks/{deckId}/cards/export")
    public ResponseEntity<ExportDTO> getCardsForExport(@PathVariable Long userId, @PathVariable Long deckId) {
        Optional<String> deckName = deckService.getDeckNameByIdAndUserId(userId, deckId);
        Optional<List<CardExportDTO>> cardExportDTOS = exportService.getDeckForExport(userId, deckId);
        if (cardExportDTOS.isEmpty() || deckName.isEmpty())
            return ResponseEntity.notFound().build();

        if (cardExportDTOS.get().isEmpty())
            return ResponseEntity.ok(new ExportDTO(deckName.get(), List.of()));

        return ResponseEntity.ok(new ExportDTO(deckName.get(), cardExportDTOS.get()));
    }

    @PostMapping("/users/{userId}/decks/import")
    public ResponseEntity<?> importDeck(@PathVariable Long userId, @RequestBody ExportDTO exportDTO) {
        if (!deckService.createDeck(userId, new DeckNameDTO(exportDTO.deckName())))
            return ResponseEntity.internalServerError().build();

        Optional<Deck> deck = deckService.findTopByOwnerIdAndNameOrderByIdDesc(userId, exportDTO.deckName());
        if (deck.isEmpty())
            return ResponseEntity.internalServerError().build();

        exportDTO.cardExportDTOList().forEach(dto -> {
            if (dto instanceof TextAnswerDTO textAnswerDTO) {
                cardService.saveCard(objectMapper.valueToTree(new TextAnswerCardDTO(new CardDTO(textAnswerDTO.getCardDTO().question(), textAnswerDTO.getCardDTO().image()), textAnswerDTO.getTextAnswer(), textAnswerDTO.getImage())), 1L, deck.get().getId());
            }
            else if (dto instanceof MultipleChoiceCardDTO multipleChoiceCardDTO) {
                var jsonNode = objectMapper.valueToTree(new com.service.databaseservice.payload.import_function.MultipleChoiceCardDTO(new CardDTO(multipleChoiceCardDTO.getCardDTO().question(), multipleChoiceCardDTO.getCardDTO().image()),
                        multipleChoiceCardDTO.getChoiceAnswers().stream().map(choiceAnswerDTO -> new ChoiceAnswerDTO(choiceAnswerDTO.answer(), choiceAnswerDTO.getIsRightAnswer(), choiceAnswerDTO.image())).collect(Collectors.toList())));
                cardService.saveCard(jsonNode, userId, deck.get().getId());
            }
        });
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/users/{userId}/decks/{deckId}/exists")
    public ResponseEntity<?> existsDeckByUserIdAndDeckId(@PathVariable Long userId, @PathVariable Long deckId) {
        return deckService.existsByDeckIdAndUserId(deckId, userId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/decks/share/{token}")
    public ResponseEntity<?> cloneSharedDeck(@PathVariable String token) {
        Optional<User> userOptional =userTokenService.getUserByUserToken(token);
        if(userOptional.isEmpty() || !userTokenService.isUserTokenValid(token))
            return ResponseEntity.notFound().build();

        Optional<Long> deckId = userTokenService.getDeckIdByUserToken(token);

        if(deckId.isEmpty())
            return ResponseEntity.notFound().build();

        boolean wasCloned = deckService.cloneSharedDeck(userOptional.get().getId(), deckId.get());

        if(wasCloned && userTokenService.deleteToken(userOptional.get().getId(), token))
            return ResponseEntity.status(HttpStatus.CREATED).build();
        else
            return ResponseEntity.internalServerError().build();
    }
}
