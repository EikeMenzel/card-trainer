package com.service.databaseservice.services;

import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.User;
import com.service.databaseservice.model.cards.Card;
import com.service.databaseservice.model.cards.CardType;
import com.service.databaseservice.payload.inc.DeckNameDTO;
import com.service.databaseservice.payload.out.DeckDTO;
import com.service.databaseservice.repository.DeckRepository;
import com.service.databaseservice.repository.ImageRepository;
import com.service.databaseservice.repository.cards.CardRepository;
import com.service.databaseservice.repository.cards.MultipleChoiceCardRepository;
import com.service.databaseservice.repository.cards.TextAnswerCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DeckService.class)
class DeckServiceTests {

    @MockBean
    private DeckRepository deckRepository;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private TextAnswerCardRepository textAnswerCardRepository;

    @MockBean
    private MultipleChoiceCardRepository multipleChoiceCardRepository;

    @MockBean
    private ImageRepository imageRepository;

    @MockBean
    private RepetitionService repetitionService;

    @Autowired
    private DeckService deckService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDecksByUserId() {
        User user = new User(1L, "X", "x", "x", false, false, 10, "en");
        List<Deck> decks = new ArrayList<>();
        decks.add(new Deck( "Deck 1", user));
        decks.add(new Deck( "Deck 2", user));

        when(deckRepository.getAllByOwnerId(user.getId())).thenReturn(decks);

        List<DeckDTO> result = deckService.getAllDecksByUserId(user.getId());

        assertEquals(2, result.size());
        assertEquals("Deck 1", result.get(0).deckName());
        assertEquals("Deck 2", result.get(1).deckName());
    }

    @Test
    void testGetDeckCountByUserId() {
        User user = new User(1L, "X", "x", "x", false, false, 10, "en");
        List<Deck> decks = new ArrayList<>();
        decks.add(new Deck( "Deck 1", user));
        decks.add(new Deck( "Deck 2", user));

        when(deckRepository.getAllByOwnerId(user.getId())).thenReturn(decks);

        Integer result = deckService.getDeckCountByUserId(user.getId());

        assertEquals(2, result);
    }

    @Test
    void testGetDeckByIdAndUserId() {
        User user = new User(1L, "X", "x", "x", false, false, 10, "en");

        Deck deck1 = new Deck( "Deck 1", user);

        when(deckRepository.getDeckByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(deck1));

        Optional<DeckDTO> result = deckService.getDeckByIdAndUserId(user.getId(), 1L);

        assertTrue(result.isPresent());
        assertEquals("Deck 1", result.get().deckName());
    }

    @Test
    void testGetDeckNameByIdAndUserId() {
        User user = new User(1L, "X", "x", "x", false, false, 10, "en");

        Deck deck1 = new Deck( "Deck 1", user);

        when(deckRepository.getDeckByIdAndOwnerId(1L, user.getId())).thenReturn(Optional.of(deck1));

        Optional<String> result = deckService.getDeckNameByIdAndUserId(user.getId(), 1L);

        assertTrue(result.isPresent());
        assertEquals("Deck 1", result.get());
    }

    @Test
    void testGetDeckNameById() {
        User user = new User(1L, "X", "x", "x", false, false, 10, "en");

        Deck deck1 = new Deck( "Deck 1", user);

        when(deckRepository.findById(1L)).thenReturn(Optional.of(deck1));

        Optional<String> result = deckService.getDeckNameById(1L);

        assertTrue(result.isPresent());
        assertEquals("Deck 1", result.get());
    }

    @Test
    void testExistsByDeckIdAndUserId() {
        Long deckId = 1L;
        Long userId = 1L;

        when(deckRepository.existsDeckByIdAndOwnerId(deckId, userId)).thenReturn(true);

        boolean result = deckService.existsByDeckIdAndUserId(deckId, userId);

        assertTrue(result);
    }

    @Test
    void testCreateDeck() {
        User user = new User(1L, "X", "x", "x", false, false, 10, "en");
        DeckNameDTO deckNameDTO = new DeckNameDTO("New Deck");

        when(userService.getUserFromId(user.getId())).thenReturn(Optional.of(user));

        boolean result = deckService.createDeck(user.getId(), deckNameDTO);

        assertTrue(result);
        verify(deckRepository, times(1)).save(any());
    }

    @Test
    void testCreateDeckUserNotFound() {
        Long userId = 1L;
        DeckNameDTO deckNameDTO = new DeckNameDTO("New Deck");

        when(userService.getUserFromId(userId)).thenReturn(Optional.empty());

        boolean result = deckService.createDeck(userId, deckNameDTO);

        assertFalse(result);
        verify(deckRepository, never()).save(any());
    }

    @Test
    void testDeleteDeck() {
        Long userId = 1L;
        Long deckId = 1L;

        when(deckRepository.existsDeckByIdAndOwnerId(deckId, userId)).thenReturn(true);

        boolean result = deckService.deleteDeck(userId, deckId);

        assertTrue(result);
        verify(deckRepository, times(1)).deleteById(deckId);
    }

    @Test
    void testDeleteDeckNotExists() {
        Long userId = 1L;
        Long deckId = 1L;

        when(deckRepository.existsDeckByIdAndOwnerId(deckId, userId)).thenReturn(false);

        boolean result = deckService.deleteDeck(userId, deckId);

        assertFalse(result);
        verify(deckRepository, never()).deleteById(deckId);
    }

    @Test
    void testUpdateDeckInformationNotExists() {
        Long userId = 1L;
        Long deckId = 1L;
        DeckNameDTO deckNameDTO = new DeckNameDTO("Updated Deck Name");

        when(deckRepository.getDeckByIdAndOwnerId(deckId, userId)).thenReturn(Optional.empty());

        boolean result = deckService.updateDeckInformation(userId, deckId, deckNameDTO);

        assertFalse(result);
        verify(deckRepository, never()).save(any(Deck.class));
    }

    @Test
    void testFindTopByOwnerIdAndNameOrderByIdDesc() {
        Long userId = 1L;
        String deckName = "Test Deck";
        User user = new User(userId, "X", "x", "x", false, false, 10, "en");
        Deck deck1 = new Deck( deckName, user);

        when(deckRepository.findTopByOwnerIdAndNameOrderByIdDesc(userId, deckName)).thenReturn(Optional.of(deck1));

        Optional<Deck> result = deckService.findTopByOwnerIdAndNameOrderByIdDesc(userId, deckName);

        assertTrue(result.isPresent());
        assertEquals(deckName, result.get().getName());
    }

    @Test
    void testCloneSharedDeck() {
        Long userId = 1L;
        Long deckId = 1L;
        String deckName = "Test Deck";
        User user = new User(userId, "X", "x", "x", false, false, 10, "en");
        Deck deck1 = new Deck(deckName, user);

        when(deckRepository.findById(deckId)).thenReturn(Optional.of(deck1));
        when(userService.getUserFromId(userId)).thenReturn(Optional.of(user));
        when(deckRepository.save(any(Deck.class))).thenAnswer(invocation -> {
            Deck originalDeck = invocation.getArgument(0);
            return new Deck(originalDeck.getId(), "Cloned Deck", user);
        });

        when(cardRepository.getCardsByDeckId(deckId)).thenReturn(Collections.emptyList());

        boolean result = deckService.cloneSharedDeck(userId, deckId);

        assertTrue(result);
        verify(deckRepository, times(1)).findById(deckId);
        verify(userService, times(1)).getUserFromId(userId);
        verify(deckRepository, times(1)).save(any(Deck.class));
        verify(cardRepository, times(1)).getCardsByDeckId(deckId);
        verify(textAnswerCardRepository, never()).getTextAnswerCardById(anyLong());
        verify(multipleChoiceCardRepository, never()).findById(anyLong());
    }

    @Test
    void testCloneSharedDeckWithCards() {
        Long userId = 1L;
        Long deckId = 1L;
        String deckName = "Test Deck";
        User user = new User(userId, "X", "x", "x", false, false, 10, "en");
        Deck deck1 = new Deck(deckName, user);

        when(deckRepository.findById(deckId)).thenReturn(Optional.of(deck1));
        when(userService.getUserFromId(userId)).thenReturn(Optional.of(user));
        when(deckRepository.save(any(Deck.class))).thenAnswer(invocation -> {
            Deck originalDeck = invocation.getArgument(0);
            return new Deck(originalDeck.getId(), "Cloned Deck", user);
        });

        List<Card> originalCards = Arrays.asList(
                new Card(1L, "Card 1", null, deck1, new CardType("BASIC")),
                new Card(2L, "Card 2", null, deck1, new CardType("BASIC"))
        );
        when(cardRepository.getCardsByDeckId(deckId)).thenReturn(originalCards);

        when(textAnswerCardRepository.getTextAnswerCardById(1L)).thenReturn(Optional.empty());
        when(textAnswerCardRepository.getTextAnswerCardById(2L)).thenReturn(Optional.empty());

        when(multipleChoiceCardRepository.findById(1L)).thenReturn(Optional.empty());
        when(multipleChoiceCardRepository.findById(2L)).thenReturn(Optional.empty());

        deckService.cloneSharedDeck(userId, deckId);

        verify(deckRepository, times(1)).findById(deckId);
        verify(userService, times(1)).getUserFromId(userId);
        verify(deckRepository, times(1)).save(any(Deck.class));
        verify(cardRepository, times(1)).getCardsByDeckId(deckId);
    }

    @Test
    void testCloneSharedDeckUserNotFound() {
        Long userId = 1L;
        Long deckId = 1L;
        String deckName = "Test Deck";
        User user = new User(userId, "X", "x", "x", false, false, 10, "en");
        Deck deck1 = new Deck(deckName, user);

        when(deckRepository.findById(deckId)).thenReturn(Optional.of(deck1));

        when(userService.getUserFromId(userId)).thenReturn(Optional.empty());

        boolean result = deckService.cloneSharedDeck(userId, deckId);

        assertFalse(result);
        verify(deckRepository, times(1)).findById(deckId);
        verify(userService, times(1)).getUserFromId(userId);
        verify(deckRepository, never()).save(any(Deck.class));
        verify(cardRepository, never()).getCardsByDeckId(deckId);
        verify(textAnswerCardRepository, never()).getTextAnswerCardById(anyLong());
        verify(multipleChoiceCardRepository, never()).findById(anyLong());
    }

    @Test
    void testCloneSharedDeckDeckNotFound() {
        Long userId = 1L;
        Long deckId = 1L;

        when(deckRepository.findById(deckId)).thenReturn(Optional.empty());

        boolean result = deckService.cloneSharedDeck(userId, deckId);

        assertFalse(result);
        verify(deckRepository, times(1)).findById(deckId);
        verify(userService, never()).getUserFromId(userId);
        verify(deckRepository, never()).save(any(Deck.class));
        verify(cardRepository, never()).getCardsByDeckId(deckId);
        verify(textAnswerCardRepository, never()).getTextAnswerCardById(anyLong());
        verify(multipleChoiceCardRepository, never()).findById(anyLong());
    }
}
