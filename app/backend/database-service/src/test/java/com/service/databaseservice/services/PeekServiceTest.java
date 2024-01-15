package com.service.databaseservice.services;

import com.service.databaseservice.model.*;
import com.service.databaseservice.model.cards.Card;
import com.service.databaseservice.model.sessions.PeekSession;
import com.service.databaseservice.model.sessions.PeekSessionCards;
import com.service.databaseservice.model.sessions.StatusType;
import com.service.databaseservice.payload.inc.learnsession.StatusTypeDTO;
import com.service.databaseservice.repository.*;
import com.service.databaseservice.repository.cards.CardRepository;
import com.service.databaseservice.repository.sessions.PeekSessionCardsRepository;
import com.service.databaseservice.repository.sessions.PeekSessionRepository;
import com.service.databaseservice.repository.sessions.StatusTypeRepository;
import com.service.databaseservice.services.CardService;
import com.service.databaseservice.services.PeekSessionService;
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

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PeekSessionService.class)
class PeekSessionServiceTest {

    @MockBean
    private StatusTypeRepository statusTypeRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private DeckRepository deckRepository;

    @MockBean
    private PeekSessionRepository peekSessionRepository;

    @MockBean
    private PeekSessionCardsRepository peekSessionCardsRepository;

    @MockBean
    private CardService cardService;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private Random random;

    @Autowired
    private PeekSessionService peekSessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPeekSession_Success() {
        Long userId = 1L;
        Long deckId = 1L;
        StatusType statusType = new StatusType(1L, "Not Finished");
        User user = new User();
        Deck deck = new Deck();

        when(statusTypeRepository.findById(1L)).thenReturn(Optional.of(statusType));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(deckRepository.findById(deckId)).thenReturn(Optional.of(deck));
        when(peekSessionRepository.save(any(PeekSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        peekSessionService.createPeekSession(userId, deckId);

        verify(peekSessionRepository).save(any(PeekSession.class));
    }

    @Test
    void createPeekSession_MissingUser() {
        Long userId = 1L, deckId = 2L;
        Deck deck = new Deck();
        StatusType statusType = new StatusType();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(deckRepository.findById(deckId)).thenReturn(Optional.of(deck));
        when(statusTypeRepository.findById(1L)).thenReturn(Optional.of(statusType));

        Optional<Long> result = peekSessionService.createPeekSession(userId, deckId);

        assertFalse(result.isPresent());
        verify(peekSessionRepository, never()).save(any(PeekSession.class));
    }

    @Test
    void createPeekSession_WithValidData_ReturnsPeekSessionId() {
        Long userId = 1L, deckId = 1L;
        User user = new User();
        Deck deck = new Deck();
        StatusType statusType = new StatusType();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(deckRepository.findById(deckId)).thenReturn(Optional.of(deck));
        when(statusTypeRepository.findById(1L)).thenReturn(Optional.of(statusType));
        when(peekSessionRepository.save(any(PeekSession.class))).thenAnswer(i -> i.getArgument(0));

        Optional<Long> result = peekSessionService.createPeekSession(userId, deckId);

        verify(peekSessionRepository).save(any(PeekSession.class));
    }

    @Test
    void doesPeekSessionFromUserExist_SessionExists() {
        Long userId = 1L, peekSessionId = 2L;
        PeekSession peekSession = new PeekSession();

        when(peekSessionRepository.getPeekSessionByIdAndUserId(peekSessionId, userId)).thenReturn(Optional.of(peekSession));

        assertTrue(peekSessionService.doesPeekSessionFromUserExist(userId, peekSessionId));
    }

    @Test
    void doesPeekSessionFromUserExist_SessionDoesNotExist() {
        Long userId = 1L, peekSessionId = 2L;

        when(peekSessionRepository.getPeekSessionByIdAndUserId(peekSessionId, userId)).thenReturn(Optional.empty());

        assertFalse(peekSessionService.doesPeekSessionFromUserExist(userId, peekSessionId));
    }

    @Test
    void getRandomCardToLearn_NoCardsAvailable() {
        Long peekSessionId = 1L;
        User user = new User(1L, "X" , "x", "x", false, false, 10, "en");
        Deck deck = new Deck("deckName", user);
        PeekSession peekSession = new PeekSession(peekSessionId, new StatusType(1L, "Nope"), deck, user);

        when(peekSessionRepository.findById(peekSessionId)).thenReturn(Optional.of(peekSession));

        when(cardRepository.getCardsByDeckId(deck.getId())).thenReturn(List.of());

        Optional<Object> result = peekSessionService.getRandomCardToLearn(peekSessionId);

        assertFalse(result.isPresent());
    }

    @Test
    void updateStatusTypeInPeekSession_Success() {
        Long peekSessionId = 1L;
        StatusTypeDTO statusTypeDTO = StatusTypeDTO.FINISHED;
        StatusType statusType = new StatusType();
        PeekSession peekSession = new PeekSession();

        when(peekSessionRepository.findById(peekSessionId)).thenReturn(Optional.of(peekSession));
        when(statusTypeRepository.findById(statusTypeDTO.getFieldId())).thenReturn(Optional.of(statusType));

        assertTrue(peekSessionService.updateStatusTypeInPeekSession(peekSessionId, statusTypeDTO));
        verify(peekSessionRepository).save(any(PeekSession.class));
    }

    @Test
    void updateStatusTypeInPeekSession_MissingData() {
        Long peekSessionId = 1L;
        StatusTypeDTO statusTypeDTO = StatusTypeDTO.FINISHED;

        when(peekSessionRepository.findById(peekSessionId)).thenReturn(Optional.empty());

        assertFalse(peekSessionService.updateStatusTypeInPeekSession(peekSessionId, statusTypeDTO));
        verify(peekSessionRepository, never()).save(any(PeekSession.class));
    }

    @Test
    void savePeekSessionCard_Success() {
        Long peekSessionId = 1L, cardId = 2L, userId = 3L;
        User user = new User(1L, "X" , "x", "x", false, false, 10, "en");
        Deck deck = new Deck("deckName", user);
        PeekSession peekSession = new PeekSession(peekSessionId, new StatusType(1L, "Nope"), deck, user);
        Card card = new Card();

        when(peekSessionRepository.findById(peekSessionId)).thenReturn(Optional.of(peekSession));
        when(cardService.doesCardBelongToOwnerAndDeck(userId, peekSession.getDeck().getId(), cardId)).thenReturn(true);
        when(cardRepository.getCardById(cardId)).thenReturn(Optional.of(card));

        assertTrue(peekSessionService.savePeekSessionCard(peekSessionId, cardId, userId));
        verify(peekSessionCardsRepository).save(any(PeekSessionCards.class));
    }

    @Test
    void savePeekSessionCard_InvalidCardOrUser() {
        Long peekSessionId = 1L, cardId = 2L, userId = 3L;
        User user = new User(1L, "X" , "x", "x", false, false, 10, "en");
        Deck deck = new Deck("deckName", user);
        PeekSession peekSession = new PeekSession(peekSessionId, new StatusType(1L, "Nope"), deck, user);

        when(peekSessionRepository.findById(peekSessionId)).thenReturn(Optional.of(peekSession));
        when(cardService.doesCardBelongToOwnerAndDeck(userId, peekSession.getDeck().getId(), cardId)).thenReturn(false);

        assertFalse(peekSessionService.savePeekSessionCard(peekSessionId, cardId, userId));
        verify(peekSessionCardsRepository, never()).save(any(PeekSessionCards.class));
    }

    @Test
    void savePeekSessionCard_PeekSessionNotFound() {
        Long peekSessionId = 1L, cardId = 2L, userId = 3L;

        when(peekSessionRepository.findById(peekSessionId)).thenReturn(Optional.empty());

        assertFalse(peekSessionService.savePeekSessionCard(peekSessionId, cardId, userId));
        verify(peekSessionCardsRepository, never()).save(any(PeekSessionCards.class));
    }

    @Test
    void savePeekSessionCard_CardNotFound() {
        Long peekSessionId = 1L, cardId = 2L, userId = 3L;
        User user = new User(1L, "X" , "x", "x", false, false, 10, "en");
        Deck deck = new Deck("deckName", user);
        PeekSession peekSession = new PeekSession(peekSessionId, new StatusType(1L, "Nope"), deck, user);

        when(peekSessionRepository.findById(peekSessionId)).thenReturn(Optional.of(peekSession));
        when(cardService.doesCardBelongToOwnerAndDeck(userId, peekSession.getDeck().getId(), cardId)).thenReturn(true);
        when(cardRepository.getCardById(cardId)).thenReturn(Optional.empty());

        assertFalse(peekSessionService.savePeekSessionCard(peekSessionId, cardId, userId));
        verify(peekSessionCardsRepository, never()).save(any(PeekSessionCards.class));
    }
}