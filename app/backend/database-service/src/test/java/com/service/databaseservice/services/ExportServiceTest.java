package com.service.databaseservice.services;

import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.User;
import com.service.databaseservice.model.cards.Card;
import com.service.databaseservice.model.cards.CardType;
import com.service.databaseservice.payload.out.export.CardExportDTO;
import com.service.databaseservice.payload.out.export.MultipleChoiceCardDTO;
import com.service.databaseservice.payload.out.export.TextAnswerDTO;
import com.service.databaseservice.repository.DeckRepository;
import com.service.databaseservice.repository.cards.CardRepository;
import com.service.databaseservice.repository.cards.MultipleChoiceCardRepository;
import com.service.databaseservice.repository.cards.TextAnswerCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

class ExportServiceTest {

    @Mock
    private DeckRepository deckRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private TextAnswerCardRepository textAnswerCardRepository;
    @Mock
    private MultipleChoiceCardRepository multipleChoiceCardRepository;

    private ExportService exportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exportService = new ExportService(deckRepository, cardRepository, textAnswerCardRepository, multipleChoiceCardRepository);
    }

    @Test
    void getDeckForExport_ExistingDeck_ReturnsListOfCardExportDTO_InavlidCards() {
        // Arrange
        Long userId = 1L;
        Long deckId = 2L;
        when(deckRepository.existsDeckByIdAndOwnerId(deckId, userId)).thenReturn(true);

        User user = new User(1L, "X", "x", "x", false, false, 10, "en");
        Deck deck = new Deck("deckName", user);

        List<Card> cards = new ArrayList<>();
        cards.add(new Card("Question 1", null, deck, new CardType("BASIC")));
        cards.add(new Card("Question 2", null, deck, new CardType("MULTIPLE_CHOICE")));

        Mockito.when(deckRepository.existsDeckByIdAndOwnerId(anyLong(), anyLong())).thenReturn(true);
        Mockito.when(cardRepository.getCardsByDeckId(anyLong())).thenReturn(cards);

        Optional<List<CardExportDTO>> result = exportService.getDeckForExport(userId, deckId);

        assertTrue(result.isPresent());
        List<CardExportDTO> cardExportDTOs = result.get();
        assertEquals(0, cardExportDTOs.size());
    }

    @Test
    void getDeckForExport_NonExistingDeck_ReturnsEmptyOptional() {
        // Arrange
        Long userId = 1L;
        Long deckId = 2L;
        when(deckRepository.existsDeckByIdAndOwnerId(deckId, userId)).thenReturn(false);

        Optional<List<CardExportDTO>> result = exportService.getDeckForExport(userId, deckId);

        assertTrue(result.isEmpty());
    }

}
