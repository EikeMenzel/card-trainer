package com.service.databaseservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.Image;
import com.service.databaseservice.model.User;
import com.service.databaseservice.model.cards.*;
import com.service.databaseservice.payload.savecard.CardDTO;
import com.service.databaseservice.payload.savecard.ChoiceAnswerDTO;
import com.service.databaseservice.payload.savecard.MultipleChoiceCardDTO;
import com.service.databaseservice.repository.DeckRepository;
import com.service.databaseservice.repository.ImageRepository;
import com.service.databaseservice.repository.RepetitionRepository;
import com.service.databaseservice.repository.UserRepository;
import com.service.databaseservice.repository.cards.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CardService.class)
class CardServiceTest {

    @MockBean
    private CardTypeRepository cardTypeRepository;

    @MockBean
    private DeckRepository deckRepository;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private MultipleChoiceCardRepository multipleChoiceCardRepository;

    @MockBean
    private ChoiceAnswerRepository choiceAnswerRepository;

    @MockBean
    private TextAnswerCardRepository textAnswerCardRepository;

    @MockBean
    private ImageRepository imageRepository;

    @MockBean
    private RepetitionRepository repetitionRepository;
    @MockBean
    private RepetitionService repetitionService;

    @MockBean
    private ObjectMapper objectMapper;

    @Autowired
    private CardService cardService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoesCardBelongToOwner_True() {
        Long userId = 1L;
        User user = new User(userId, "username", "email@example.com", "password",false, false, 10, "en");
        Deck deck = new Deck(1L, "Test Deck", user);
        Card card = new Card(1L, "Question?", null, deck, null);

        when(cardRepository.findById(any(Long.class))).thenReturn(Optional.of(card));

        assertTrue(cardService.doesCardBelongToOwner(card.getId(), userId));
    }

    @Test
    void testDoesCardBelongToOwner_CardNotFound() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        assertFalse(cardService.doesCardBelongToOwner(1L, 1L));
    }

    @Test
    void testDoesCardBelongToOwnerAndDeck_True() {
        Long userId = 1L;
        Long deckId = 1L;
        Long cardId = 1L;

        User user = new User(userId, "username", "email@example.com", "password", false, false, 10, "en");
        Deck deck = new Deck("Test Deck", user);
        CardType cardType = new CardType("BASIC");
        Card card = new Card("Question?", null, deck, cardType);

        // Mocking repository behavior
        when(deckRepository.getDeckByIdAndOwnerId(any(), any())).thenReturn(Optional.of(deck));
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Execute the test
        boolean result = cardService.doesCardBelongToOwnerAndDeck(userId, deckId, cardId);

        // Assert the result
        assertTrue(result);
    }

    @Test
    void testDoesCardBelongToOwnerAndDeck_CardNotBelongToUser() {
        Long userId = 1L;
        Long otherUserId = 2L;
        Long deckId = 1L;
        Long cardId = 1L;
        Deck deck = new Deck("Test Deck", new User("otherUser", "other@email.com", "pass", true));
        Card card = new Card("Question?", null, deck, null);

        when(deckRepository.getDeckByIdAndOwnerId(deckId, otherUserId)).thenReturn(Optional.of(deck));
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        assertFalse(cardService.doesCardBelongToOwnerAndDeck(userId, deckId, cardId));
    }

    @Test
    void testDoesCardBelongToOwnerAndDeck_CardNotInDeck() {
        Long userId = 1L;
        Long deckId = 1L;
        Long cardId = 1L;
        User user = new User(userId, "username", "email@example.com", "password",false, false, 10, "en");
        Deck deck = new Deck("Test Deck", user);
        Deck otherDeck = new Deck("Other Deck", user);
        Card card = new Card("Question?", null, otherDeck, null);

        when(deckRepository.getDeckByIdAndOwnerId(deckId, userId)).thenReturn(Optional.of(deck));
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        assertFalse(cardService.doesCardBelongToOwnerAndDeck(userId, deckId, cardId));
    }

    @Test
    void testDoesCardBelongToOwnerAndDeck_NonExistentDeckOrCard() {
        Long userId = 1L;
        Long deckId = 1L;
        Long cardId = 1L;

        when(deckRepository.getDeckByIdAndOwnerId(deckId, userId)).thenReturn(Optional.empty());
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertFalse(cardService.doesCardBelongToOwnerAndDeck(userId, deckId, cardId));
    }

    @Test
    void testGetCardAmountFromDeckId_WithMultipleCards() {
        Long deckId = 1L;
        int expectedCount = 5; // Example count when multiple cards are in the deck
        when(cardRepository.countCardsByDeckId(deckId)).thenReturn(expectedCount);

        assertEquals(expectedCount, cardService.getCardAmountFromDeckId(deckId));
    }

    @Test
    void testGetCardAmountFromDeckId_WithNoCards() {
        Long deckId = 1L;
        when(cardRepository.countCardsByDeckId(deckId)).thenReturn(0); // Deck has no cards

        assertEquals(0, cardService.getCardAmountFromDeckId(deckId));
    }

    @Test
    void testGetCardAmountFromDeckId_InvalidDeckId() {
        Long deckId = 99L; // Assuming this is an invalid/non-existent deck ID
        when(cardRepository.countCardsByDeckId(deckId)).thenReturn(0); // No cards for invalid deck

        assertEquals(0, cardService.getCardAmountFromDeckId(deckId));
    }

    @Test
    void testGetCardsByDeckId_WithCards() {
        Long deckId = 1L;

        List<Card> mockCardList = Arrays.asList(new Card("Question 1", null, new Deck(), new CardType()),
                new Card("Question 2", null, new Deck(), new CardType()));
        when(cardRepository.getCardsByDeckId(deckId)).thenReturn(mockCardList);

        List<Card> result = cardService.getCardsByDeckId(deckId);

        assertEquals(mockCardList.size(), result.size());
        assertEquals(mockCardList, result);
    }

    @Test
    void testGetCardsByDeckId_NoCards() {
        Long deckId = 1L;
        when(cardRepository.getCardsByDeckId(deckId)).thenReturn(Collections.emptyList());

        List<Card> result = cardService.getCardsByDeckId(deckId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetCardsFromDeckId_WithCards() {
        Long deckId = 1L;
        List<Card> mockCardList = Arrays.asList(new Card("Question 1", null, new Deck(), new CardType()),
                new Card("Question 2", null, new Deck(), new CardType()));
        when(cardRepository.getCardsByDeckId(deckId)).thenReturn(mockCardList);

        var result = cardService.getCardsFromDeckId(deckId);

        assertEquals(mockCardList.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(mockCardList.get(i).getQuestion(), result.get(i).question());
            assertEquals(mockCardList.get(i).getCardType().getType(), result.get(i).type());
        }
    }

    @Test
    void testGetCardsFromDeckId_NoCards() {
        Long deckId = 1L;
        when(cardRepository.getCardsByDeckId(deckId)).thenReturn(Collections.emptyList());

        var result = cardService.getCardsFromDeckId(deckId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveCard_InvalidCardData() {
        Long userId = 1L;
        Long deckId = 1L;
        JsonNode invalidCardNode = new ObjectMapper().createObjectNode(); // Empty JSON Node

        boolean result = cardService.saveCard(invalidCardNode, userId, deckId);

        assertFalse(result);
    }

    @Test
    void testSaveMultipleChoiceCard_CardTypeNotFound() throws JsonProcessingException {
        Long userId = 1L;
        Long deckId = 1L;
        MultipleChoiceCardDTO multipleChoiceCardDTO = new MultipleChoiceCardDTO(
                new CardDTO("Question?", null),
                Arrays.asList(
                        new ChoiceAnswerDTO("Answer 1", true, null),
                        new ChoiceAnswerDTO("Answer 2", false, null)
                )
        );

        when(cardTypeRepository.getCardTypesByType("MULTIPLE_CHOICE")).thenReturn(Optional.empty());

        String cardData = new ObjectMapper().writeValueAsString(multipleChoiceCardDTO);
        JsonNode cardNode = new ObjectMapper().readTree(cardData);

        boolean result = cardService.saveCard(cardNode, userId, deckId);

        assertFalse(result);
    }

    @Test
    void testSaveMultipleChoiceCard_DeckNotFound() throws JsonProcessingException {
        Long userId = 1L;
        Long deckId = 1L;
        MultipleChoiceCardDTO multipleChoiceCardDTO = new MultipleChoiceCardDTO(
                new CardDTO("Question?", null),
                Arrays.asList(
                        new ChoiceAnswerDTO("Answer 1", true, null),
                        new ChoiceAnswerDTO("Answer 2", false, null)
                )
        );

        CardType cardType = new CardType("MULTIPLE_CHOICE");

        when(cardTypeRepository.getCardTypesByType("MULTIPLE_CHOICE")).thenReturn(Optional.of(cardType));
        when(deckRepository.getDeckByIdAndOwnerId(deckId, userId)).thenReturn(Optional.empty());

        String cardData = new ObjectMapper().writeValueAsString(multipleChoiceCardDTO);
        JsonNode cardNode = new ObjectMapper().readTree(cardData);

        boolean result = cardService.saveCard(cardNode, userId, deckId);

        assertFalse(result);
    }


    @Test
    void testUpdateMultipleChoiceCard_CardNotFound() {
        Long cardId = 1L;
        Long userId = 1L;
        Long multipleChoiceCardId = 2L;
        com.service.databaseservice.payload.inc.updatecard.MultipleChoiceCardDTO multipleChoiceCardDTO = new com.service.databaseservice.payload.inc.updatecard.MultipleChoiceCardDTO(
                multipleChoiceCardId,
                new com.service.databaseservice.payload.inc.updatecard.CardDTO(cardId, "Updated Question?", null),
                Arrays.asList(
                        new com.service.databaseservice.payload.inc.updatecard.ChoiceAnswerDTO(1L, "Updated Answer 1", true, null),
                        new com.service.databaseservice.payload.inc.updatecard.ChoiceAnswerDTO(2L, "Updated Answer 2", false, null)
                )
        );

        when(cardRepository.getCardById(cardId)).thenReturn(Optional.empty());

        boolean result = cardService.updateMultipleChoiceCard(cardId, multipleChoiceCardDTO, userId);

        assertFalse(result);
    }

    @Test
    void testUpdateMultipleChoiceCard_MultipleChoiceCardNotFound() {
        Long cardId = 1L;
        Long userId = 1L;
        Long multipleChoiceCardId = 2L;
        com.service.databaseservice.payload.inc.updatecard.MultipleChoiceCardDTO multipleChoiceCardDTO = new com.service.databaseservice.payload.inc.updatecard.MultipleChoiceCardDTO(
                multipleChoiceCardId,
                new com.service.databaseservice.payload.inc.updatecard.CardDTO(cardId, "Updated Question?", null),
                Arrays.asList(
                        new com.service.databaseservice.payload.inc.updatecard.ChoiceAnswerDTO(1L, "Updated Answer 1",  true, null),
                        new com.service.databaseservice.payload.inc.updatecard.ChoiceAnswerDTO(2L, "Updated Answer 2",  false, null)
                )
        );

        Card card = new Card("Question?", null, new Deck("Test Deck", new User("username", "email@example.com", "password", true)), new CardType("MULTIPLE_CHOICE"));

        when(cardRepository.getCardById(cardId)).thenReturn(Optional.of(card));
        when(multipleChoiceCardRepository.findById(multipleChoiceCardId)).thenReturn(Optional.empty());

        boolean result = cardService.updateMultipleChoiceCard(cardId, multipleChoiceCardDTO, userId);

        assertFalse(result);
    }


    @Test
    void testIsCreateChoiceAnswerResult_Success() {
        Long userId = 1L;
        Long multipleChoiceCardId = 1L;
        List<com.service.databaseservice.payload.inc.updatecard.ChoiceAnswerDTO> choiceAnswerDTOs = Arrays.asList(
                new com.service.databaseservice.payload.inc.updatecard.ChoiceAnswerDTO(null, "New Answer 1", true, null),
                new com.service.databaseservice.payload.inc.updatecard.ChoiceAnswerDTO(null, "New Answer 2", false, 1L) // Assuming image with ID 1 exists
        );

        MultipleChoiceCard multipleChoiceCard = new MultipleChoiceCard(multipleChoiceCardId);

        when(imageRepository.getImageByIdAndUserId(1L, userId)).thenReturn(Optional.of(new Image()));

        boolean result = cardService.isCreateChoiceAnswerResult(choiceAnswerDTOs, multipleChoiceCard, userId);

        assertTrue(result);
    }


    @Test
    void testDeleteChoiceAnswers_Success() {
        Long multipleChoiceCardId = 1L;
        Long choiceAnswerId1 = 1L;
        Long choiceAnswerId2 = 2L;
        Long choiceAnswerId3 = 3L;
        List<com.service.databaseservice.payload.inc.updatecard.ChoiceAnswerDTO> choiceAnswerDTOs =  Arrays.asList(
                new com.service.databaseservice.payload.inc.updatecard.ChoiceAnswerDTO(1L, "Updated Answer 1",  true, null),
                new com.service.databaseservice.payload.inc.updatecard.ChoiceAnswerDTO(2L, "Updated Answer 2",  false, null)
        );

        MultipleChoiceCard multipleChoiceCard = new MultipleChoiceCard(multipleChoiceCardId);
        List<ChoiceAnswer> allChoiceAnswers = Arrays.asList(
                new ChoiceAnswer(choiceAnswerId1, "Answer 1", null, true, multipleChoiceCard),
                new ChoiceAnswer(choiceAnswerId2, "Answer 2", null, false, multipleChoiceCard),
                new ChoiceAnswer(choiceAnswerId3, "Answer 3", null, true, multipleChoiceCard)
        );

        when(choiceAnswerRepository.getAllByMultipleChoiceCardId(multipleChoiceCardId)).thenReturn(allChoiceAnswers);

        cardService.deleteChoiceAnswers(choiceAnswerDTOs, multipleChoiceCard);
        assertFalse(choiceAnswerRepository.existsById(1L));
    }


    @Test
    void testUpdateTextAnswerCard_Success() {
        Long cardId = 1L;
        Long textAnswerCardId = 1L;
        Long userId = 1L;

        com.service.databaseservice.payload.inc.updatecard.TextAnswerCardDTO textAnswerCardDTO = new com.service.databaseservice.payload.inc.updatecard.TextAnswerCardDTO(
                textAnswerCardId,
                new com.service.databaseservice.payload.inc.updatecard.CardDTO(1L, "Updated Question", 1L),
                "Updated Answer",
                null
        );

        Card card = new Card(cardId, "Question", null, new Deck(1L, "Test",
                new User(1L, "x", "x", "x", false, false, 10, "en")), new CardType("BASIC"));
        TextAnswerCard textAnswerCard = new TextAnswerCard(textAnswerCardId, "Answer", null);

        when(cardRepository.getCardById(any())).thenReturn(Optional.of(card));
        when(textAnswerCardRepository.findById(any())).thenReturn(Optional.of(textAnswerCard));
        when(imageRepository.getImageByIdAndUserId(1L, userId)).thenReturn(Optional.of(new Image()));

        boolean result = cardService.updateTextAnswerCard(cardId, textAnswerCardDTO, userId);

        assertTrue(result);
    }

    @Test
    void testUpdateTextAnswerCard_Failure_CardNotFound() {
        Long cardId = 1L;
        Long textAnswerCardId = 1L;
        Long userId = 1L;

        com.service.databaseservice.payload.inc.updatecard.TextAnswerCardDTO textAnswerCardDTO = new com.service.databaseservice.payload.inc.updatecard.TextAnswerCardDTO(
                textAnswerCardId,
                new com.service.databaseservice.payload.inc.updatecard.CardDTO(1L, "Updated Question", null),
                "Updated Answer",
                null
        );

        when(cardRepository.getCardById(cardId)).thenReturn(Optional.empty());

        boolean result = cardService.updateTextAnswerCard(cardId, textAnswerCardDTO, userId);

        assertFalse(result);
    }

    @Test
    void testUpdateAnswerCard_Success() {
        Long textAnswerCardId = 1L;
        Card card = new Card(1L, "Question", null, new Deck(1L, "Test", new User(1L, "x", "x", "x", false, false, 10, "en")), new CardType());
        TextAnswerCard textAnswerCard = new TextAnswerCard(textAnswerCardId, "Answer", null);

        com.service.databaseservice.payload.inc.updatecard.TextAnswerCardDTO textAnswerCardDTO = new com.service.databaseservice.payload.inc.updatecard.TextAnswerCardDTO(
                textAnswerCardId,
                new com.service.databaseservice.payload.inc.updatecard.CardDTO(1L, "Question", null),
                "Updated Answer",
                null
        );

        when(imageRepository.getImageByIdAndUserId(1L, card.getDeck().getOwner().getId())).thenReturn(Optional.of(new Image()));

        boolean result = cardService.updateAnswerCard(textAnswerCardDTO, textAnswerCard, card);

        assertTrue(result);
    }

    @Test
    void testUpdateAnswerCard_True_ImageNotFound() {
        Long textAnswerCardId = 1L;
        Card card = new Card(1L, "Question", null, new Deck(1L, "Test", new User(1L, "x", "x", "x", false, false, 10, "en")), new CardType());
        TextAnswerCard textAnswerCard = new TextAnswerCard(textAnswerCardId, "Answer", null);

        com.service.databaseservice.payload.inc.updatecard.TextAnswerCardDTO textAnswerCardDTO = new com.service.databaseservice.payload.inc.updatecard.TextAnswerCardDTO(
                textAnswerCardId,
                new com.service.databaseservice.payload.inc.updatecard.CardDTO(1L, "Question", null),
                "Updated Answer",
                null
        );

        when(imageRepository.getImageByIdAndUserId(1L, card.getDeck().getOwner().getId())).thenReturn(Optional.empty());

        boolean result = cardService.updateAnswerCard(textAnswerCardDTO, textAnswerCard, card);

        assertTrue(result);
    }

    @Test
    void testUpdateBaseCard_Success() {
        Long userId = 1L;
        Card card = new Card(1L, "Question", null, new Deck(1L, "Test", new User(1L, "x", "x", "x", false, false, 10, "en")), new CardType());

        com.service.databaseservice.payload.inc.updatecard.CardDTO cardDTO = new com.service.databaseservice.payload.inc.updatecard.CardDTO(1L, "Updated Question", null);

        when(imageRepository.getImageByIdAndUserId(1L, userId)).thenReturn(Optional.of(new Image()));

        boolean result = cardService.updateBaseCard(cardDTO, card, userId);

        assertTrue(result);
    }


    @Test
    void deleteChoiceAnswerById() {
        ChoiceAnswer choiceAnswer = new ChoiceAnswer("X", null, false, new MultipleChoiceCard(1L));
        when(choiceAnswerRepository.findById(1L)).thenReturn(Optional.of(choiceAnswer));

        choiceAnswerRepository.deleteById(1L);
        assertThat(choiceAnswerRepository.count()).isZero();

        multipleChoiceCardRepository.deleteById(1L);
    }

    @Test
    void testDeleteCard_ExistingCard() {
        Long cardId = 1L;

        when(cardRepository.getCardById(cardId)).thenReturn(Optional.of(new Card()));
        doNothing().when(cardRepository).deleteById(cardId);

        boolean result = cardService.deleteCard(cardId);

        assertTrue(result);
        verify(cardRepository).deleteById(cardId);
    }

    @Test
    void testDeleteCard_NonExistingCard() {
        Long cardId = 1L;

        when(cardRepository.getCardById(cardId)).thenReturn(Optional.empty());

        boolean result = cardService.deleteCard(cardId);

        assertFalse(result);
        verify(cardRepository, never()).deleteById(cardId);
    }

    @Test
    void testGetCardDetails_NonExistingCard() {
        Long cardId = 1L;

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        Object result = cardService.getCardDetails(cardId);

        assertNull(result);
    }

    @Test
    void testGetOldestCardToLearn_NonExistingCard() {
        Long deckId = 1L;

        when(cardRepository.findOldestCardToLearn(deckId)).thenReturn(Optional.empty());

        Optional<Object> result = cardService.getOldestCardToLearn(deckId);

        assertFalse(result.isPresent());
    }

}
