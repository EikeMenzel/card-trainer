package com.service.cardsservice.services;

import com.service.cardsservice.payload.in.DeckDTO;
import com.service.cardsservice.payload.out.DeckDetailInformationDTO;
import com.service.cardsservice.payload.out.DeckInformationDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
class DeckServiceTest {
    @MockBean
    private DbQueryService dbQueryService;

    @Autowired
    private DeckService deckService;

    @Test
    void testGetAllDeckInformation() {
        Long userId = 1L;
        List<DeckDTO> expectedDecks = Arrays.asList(new DeckDTO(1L, "Deck1"), new DeckDTO(2L, "Deck2"));
        when(dbQueryService.getAllDecksByUserId(userId)).thenReturn(expectedDecks);
        when(dbQueryService.getCardsAmountByDeck(userId, 1L)).thenReturn(5);
        when(dbQueryService.getCardsAmountByDeck(userId, 2L)).thenReturn(10);
        when(dbQueryService.getCardsToLearnAmountByDeck(userId, 1L)).thenReturn(2);
        when(dbQueryService.getCardsToLearnAmountByDeck(userId, 2L)).thenReturn(4);
        when(dbQueryService.getLastLearnedTimestampByDeck(userId, 1L)).thenReturn(Optional.of(new Timestamp(System.currentTimeMillis())));
        when(dbQueryService.getLastLearnedTimestampByDeck(userId, 2L)).thenReturn(Optional.of(new Timestamp(System.currentTimeMillis())));

        List<DeckInformationDTO> result = deckService.getAllDeckInformation(userId);

        assertEquals(2, result.size());
        assertEquals("Deck1", result.get(0).getDeckName());
        assertEquals("Deck2", result.get(1).getDeckName());
    }

    @Test
    void testGetDetailInformationDeck() {
        Long userId = 1L;
        Long deckId = 1L;
        DeckDTO expectedDeck = new DeckDTO(deckId, "Deck1");
        when(dbQueryService.getDeckByUserIdAndDeckId(userId, deckId)).thenReturn(Optional.of(expectedDeck));
        when(dbQueryService.getCardsAmountByDeck(userId, deckId)).thenReturn(5);
        when(dbQueryService.getCardsToLearnAmountByDeck(userId, deckId)).thenReturn(2);
        when(dbQueryService.getLastLearnedTimestampByDeck(userId, deckId)).thenReturn(Optional.of(new Timestamp(System.currentTimeMillis())));
        List<Integer> learnState = Arrays.asList(1, 2, 3, 4, 5);
        when(dbQueryService.getLearnStateOfDeck(userId, deckId)).thenReturn(learnState);

        Optional<DeckDetailInformationDTO> result = deckService.getDetailInformationDeck(userId, deckId);

        assertTrue(result.isPresent());
        assertEquals("Deck1", result.get().getDeckName());
        assertEquals(5, result.get().getDeckSize());
        assertEquals(2, result.get().getCardsToLearn());
        assertNotNull(result.get().getLastLearned());
        assertEquals(learnState, result.get().getDeckLearnState());
    }

}
