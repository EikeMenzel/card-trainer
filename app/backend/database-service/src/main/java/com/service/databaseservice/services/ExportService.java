package com.service.databaseservice.services;

import com.service.databaseservice.model.cards.Card;
import com.service.databaseservice.model.cards.MultipleChoiceCard;
import com.service.databaseservice.model.cards.TextAnswerCard;
import com.service.databaseservice.payload.out.export.*;
import com.service.databaseservice.repository.DeckRepository;
import com.service.databaseservice.repository.cards.CardRepository;
import com.service.databaseservice.repository.cards.MultipleChoiceCardRepository;
import com.service.databaseservice.repository.cards.TextAnswerCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.service.databaseservice.services.Utils.extractImageData;

@Service
public class ExportService {
    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;
    private final TextAnswerCardRepository textAnswerCardRepository;
    private final MultipleChoiceCardRepository multipleChoiceCardRepository;

    public ExportService(DeckRepository deckRepository, CardRepository cardRepository, TextAnswerCardRepository textAnswerCardRepository, MultipleChoiceCardRepository multipleChoiceCardRepository) {
        this.deckRepository = deckRepository;
        this.cardRepository = cardRepository;
        this.textAnswerCardRepository = textAnswerCardRepository;
        this.multipleChoiceCardRepository = multipleChoiceCardRepository;
    }

    public Optional<List<CardExportDTO>> getDeckForExport(Long userId, Long deckId) {
        if (deckRepository.existsDeckByIdAndOwnerId(deckId, userId)) {
            return Optional.of(cardRepository.getCardsByDeckId(deckId)
                    .stream()
                    .map(card -> buildCardExportDTO(card.getId()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList()));
        }
        return Optional.empty();
    }

    private Optional<CardExportDTO> buildCardExportDTO(Long cardId) {
        Optional<Card> cardOptional = cardRepository.getCardById(cardId);
        if (cardOptional.isEmpty())
            return Optional.empty();

        var card = cardOptional.get();
        switch (card.getCardType().getType()) {
            case "BASIC" -> {
                return buildExportDTOFromBasicCard(card);
            }
            case "MULTIPLE_CHOICE" -> {
                return buildExportDTOFromMultipleChoiceCard(card);
            }
            default -> throw new IllegalStateException("Unexpected value: " + card.getCardType().getType());
        }
    }

    private Optional<CardExportDTO> buildExportDTOFromBasicCard(Card card) {
        Optional<TextAnswerCard> textAnswerCardOptional = textAnswerCardRepository.getTextAnswerCardById(card.getId());
        return textAnswerCardOptional.map(textAnswerCard -> new TextAnswerDTO(
                buildCardDTO(card),
                textAnswerCard.getAnswer(),
                extractImageData(textAnswerCard.getImageData())));
    }

    private Optional<CardExportDTO> buildExportDTOFromMultipleChoiceCard(Card card) {
        Optional<MultipleChoiceCard> multipleChoiceCardOptional = multipleChoiceCardRepository.findById(card.getId());
        return multipleChoiceCardOptional.map(multipleChoiceCard -> {
            List<ChoiceAnswerDTO> choiceAnswerList = multipleChoiceCard.getChoiceAnswerList()
                    .stream()
                    .map(choiceAnswer -> new ChoiceAnswerDTO(choiceAnswer.getAnswer(), extractImageData(choiceAnswer.getImageData()), choiceAnswer.getCorrect()))
                    .toList();

            return new MultipleChoiceCardDTO(
                    buildCardDTO(card),
                    choiceAnswerList
            );
        });
    }

    private CardDTO buildCardDTO(Card card) {
        return new CardDTO(card.getQuestion(), extractImageData(card.getImageData()), card.getCardType().getId());
    }
}
