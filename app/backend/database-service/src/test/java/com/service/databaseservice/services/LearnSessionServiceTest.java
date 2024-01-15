package com.service.databaseservice.services;

import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.User;
import com.service.databaseservice.model.sessions.LearnSession;
import com.service.databaseservice.model.sessions.StatusType;
import com.service.databaseservice.payload.inc.learnsession.RatingLevelDTO;
import com.service.databaseservice.payload.inc.learnsession.StatusTypeDTO;
import com.service.databaseservice.payload.out.HistoryDTO;
import com.service.databaseservice.payload.out.HistoryDetailDTO;
import com.service.databaseservice.repository.DeckRepository;
import com.service.databaseservice.repository.UserRepository;
import com.service.databaseservice.repository.sessions.LearnSessionRepository;
import com.service.databaseservice.repository.sessions.StatusTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LearnSessionService.class)
class LearnSessionServiceTest {
    @MockBean
    private LearnSessionRepository learnSessionRepository;

    @MockBean
    private StatusTypeRepository statusTypeRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private DeckRepository deckRepository;

    @Autowired
    private LearnSessionService learnSessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests for getLastLearnedFromLearnSessionById
    @Test
    void getLastLearnedFromLearnSessionById_SessionExists() {
        Long userId = 1L, deckId = 2L;
        User user = new User(1L, "X", "x", "x", false, false, 10, "en");
        Deck deck = new Deck("deckName", user);
        LearnSession learnSession = new LearnSession(user, deck, new StatusType(1L, "Nope"));
        learnSession.setFinishedAt(new Timestamp(System.currentTimeMillis()));

        when(learnSessionRepository.findTopByDeckIdAndUserIdOrderByIdDesc(deckId, userId)).thenReturn(Optional.of(learnSession));

        Optional<Timestamp> result = learnSessionService.getLastLearnedFromLearnSessionById(deckId, userId);

        assertTrue(result.isPresent());
        assertEquals(learnSession.getFinishedAt(), result.get());
    }

    @Test
    void getLastLearnedFromLearnSessionById_SessionDoesNotExist() {
        Long userId = 1L, deckId = 2L;

        when(learnSessionRepository.findTopByDeckIdAndUserIdOrderByIdDesc(deckId, userId)).thenReturn(Optional.empty());

        Optional<Timestamp> result = learnSessionService.getLastLearnedFromLearnSessionById(deckId, userId);

        assertFalse(result.isPresent());
    }

    @Test
    void getAllHistoryFromUserIdAndDeckId_HistoryExists() {
        Long userId = 1L, deckId = 2L;
        User user = new User(1L, "X", "x", "x", false, false, 10, "en");
        Deck deck = new Deck("deckName", user);
        LearnSession learnSession = new LearnSession(user, deck, new StatusType(1L, "Nope"));
        learnSession.setRating1(2);
        learnSession.setRating2(1);

        StatusType statusType = new StatusType(1L, "FINISHED");
        learnSession = learnSession.setLearnStatus(statusType);

        when(learnSessionRepository.getAllByUserIdAndDeckId(userId, deckId)).thenReturn(List.of(learnSession));

        List<HistoryDTO> historyDTOs = learnSessionService.getAllHistoryFromUserIdAndDeckId(userId, deckId);

        assertFalse(historyDTOs.isEmpty());
        assertEquals(1, historyDTOs.size());
        assertEquals(3, historyDTOs.get(0).cardsLearned());
    }

    @Test
    void getAllHistoryFromUserIdAndDeckId_NoHistory() {
        Long userId = 1L, deckId = 2L;
        when(learnSessionRepository.getAllByUserIdAndDeckId(userId, deckId)).thenReturn(List.of());

        List<HistoryDTO> historyDTOs = learnSessionService.getAllHistoryFromUserIdAndDeckId(userId, deckId);

        assertTrue(historyDTOs.isEmpty());
    }

    @Test
    void getHistoryDetailsFromHistoryIdAndUserId_HistoryDetailExists() {
        Long historyId = 1L, userId = 2L, deckId = 3L;
        User user = new User(1L, "X", "x", "x", false, false, 10, "en");
        Deck deck = new Deck("deckName", user);
        LearnSession learnSession = new LearnSession(user, deck, new StatusType(1L, "Nope"));
        learnSession.setFinishedAt(new Timestamp(System.currentTimeMillis()));
        learnSession.setRating1(2);
        learnSession.setRating2(1);

        StatusType statusType = new StatusType(1L, "FINISHED");
        learnSession = learnSession.setLearnStatus(statusType);

        when(learnSessionRepository.getLearnSessionByIdAndUserIdAndDeckId(historyId, userId, deckId)).thenReturn(Optional.of(learnSession));

        Optional<HistoryDetailDTO> historyDetailDTO = learnSessionService.getHistoryDetailsFromHistoryIdAndUserId(historyId, userId, deckId);

        assertTrue(historyDetailDTO.isPresent());
        assertEquals("deckName", historyDetailDTO.get().deckName());
    }

    @Test
    void getHistoryDetailsFromHistoryIdAndUserId_NoHistoryDetail() {
        Long historyId = 1L, userId = 2L, deckId = 3L;

        when(learnSessionRepository.getLearnSessionByIdAndUserIdAndDeckId(historyId, userId, deckId)).thenReturn(Optional.empty());

        Optional<HistoryDetailDTO> historyDetailDTO = learnSessionService.getHistoryDetailsFromHistoryIdAndUserId(historyId, userId, deckId);

        assertFalse(historyDetailDTO.isPresent());
    }

    @Test
    void createLearnSession_Success() {
        Long userId = 1L, deckId = 2L;
        User user = new User();
        Deck deck = new Deck();
        StatusType statusType = new StatusType();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(deckRepository.findById(deckId)).thenReturn(Optional.of(deck));
        when(statusTypeRepository.findById(1L)).thenReturn(Optional.of(statusType));
        when(learnSessionRepository.save(any(LearnSession.class))).thenAnswer(i -> i.getArgument(0));

        learnSessionService.createLearnSession(userId, deckId);

        verify(learnSessionRepository).save(any(LearnSession.class));
    }

    @Test
    void createLearnSession_MissingData() {
        Long userId = 1L, deckId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<Long> result = learnSessionService.createLearnSession(userId, deckId);

        assertFalse(result.isPresent());
        verify(learnSessionRepository, never()).save(any(LearnSession.class));
    }

    @Test
    void doesLearnSessionFromUserExist_SessionExists() {
        Long userId = 1L, learnSessionId = 2L;

        when(learnSessionRepository.existsLearnSessionByIdAndUserId(learnSessionId, userId)).thenReturn(true);

        assertTrue(learnSessionService.doesLearnSessionFromUserExist(userId, learnSessionId));
    }

    @Test
    void doesLearnSessionFromUserExist_SessionDoesNotExist() {
        Long userId = 1L, learnSessionId = 2L;

        when(learnSessionRepository.existsLearnSessionByIdAndUserId(learnSessionId, userId)).thenReturn(false);

        assertFalse(learnSessionService.doesLearnSessionFromUserExist(userId, learnSessionId));
    }

    @Test
    void updateRatingInLearnSession_Success() {
        Long learnSessionId = 1L;
        LearnSession learnSession = new LearnSession();
        RatingLevelDTO ratingLevelDTO = RatingLevelDTO.RATING_1;

        when(learnSessionRepository.findById(learnSessionId)).thenReturn(Optional.of(learnSession));

        assertTrue(learnSessionService.updateRatingInLearnSession(learnSessionId, ratingLevelDTO));
        assertEquals(1, learnSession.getRating2());
        verify(learnSessionRepository).save(learnSession);
    }

    @Test
    void updateRatingInLearnSession_LearnSessionNotFound() {
        Long learnSessionId = 1L;
        RatingLevelDTO ratingLevelDTO = RatingLevelDTO.RATING_1;

        when(learnSessionRepository.findById(learnSessionId)).thenReturn(Optional.empty());

        assertFalse(learnSessionService.updateRatingInLearnSession(learnSessionId, ratingLevelDTO));
    }

    @Test
    void updateStatusTypeInLearnSession_Success() {
        Long learnSessionId = 1L;
        StatusTypeDTO statusTypeDTO = StatusTypeDTO.CANCELED;
        StatusType statusType = new StatusType();
        LearnSession learnSession = new LearnSession();

        when(learnSessionRepository.findById(learnSessionId)).thenReturn(Optional.of(learnSession));
        when(statusTypeRepository.findById(statusTypeDTO.getFieldId())).thenReturn(Optional.of(statusType));

        assertTrue(learnSessionService.updateStatusTypeInLearnSession(learnSessionId, statusTypeDTO));
        verify(learnSessionRepository).save(learnSession);
    }

    @Test
    void updateStatusTypeInLearnSession_MissingData() {
        Long learnSessionId = 1L;
        StatusTypeDTO statusTypeDTO = StatusTypeDTO.CANCELED;

        when(learnSessionRepository.findById(learnSessionId)).thenReturn(Optional.empty());

        assertFalse(learnSessionService.updateStatusTypeInLearnSession(learnSessionId, statusTypeDTO));
    }

    @Test
    void getLearnSessionCount() {
        Long userId = 1L;
        User user = new User(1L, "X", "x", "x", false, false, 10, "en");
        Deck deck = new Deck("deckName", user);
        LearnSession learnSession1 = new LearnSession(user, deck, new StatusType(1L, "FINISHED"));
        LearnSession learnSession2 = new LearnSession(user, deck, new StatusType(1L, "FINISHED"));

        List<LearnSession> sessions = List.of(learnSession1, learnSession2); // Assuming these are "FINISHED" sessions

        when(learnSessionRepository.getAllByUserId(userId)).thenReturn(sessions);

        int count = learnSessionService.getLearnSessionCount(userId);
        assertEquals(2, count);

    }

    @Test
    void getCardsLearnedCount() {
        Long userId = 1L;
        LearnSession session1 = new LearnSession();
        session1.setRating1(3);
        session1.setRating2(2);

        LearnSession session2 = new LearnSession();
        session2.setRating1(4);

        when(learnSessionRepository.getAllByUserId(userId)).thenReturn(List.of(session1, session2));

        int totalCardsLearned = learnSessionService.getCardsLearnedCount(userId);

        int expectedCount = 3 + 2 + 4; // Total of ratings from both sessions
        assertEquals(expectedCount, totalCardsLearned);
    }

    @Test
    void isDailyLearnSessionCompletedToday_Completed() {
        Long userId = 1L;

        when(learnSessionRepository.isLearnSessionCompletedToday(userId)).thenReturn(true);

        assertTrue(learnSessionService.isDailyLearnSessionCompletedToday(userId));
    }

    @Test
    void isDailyLearnSessionCompletedToday_NotCompleted() {
        Long userId = 1L;

        when(learnSessionRepository.isLearnSessionCompletedToday(userId)).thenReturn(false);

        assertFalse(learnSessionService.isDailyLearnSessionCompletedToday(userId));
    }

    @Test
    void getCardsLearnedToday() {
        Long userId = 1L;
        LearnSession sessionToday = new LearnSession();
        sessionToday.setRating1(2); // Assume 2 cards learned today at Rating 1

        when(learnSessionRepository.findLearnSessionsFinishedTodayByUserId(userId)).thenReturn(List.of(sessionToday));

        int cardsLearnedToday = learnSessionService.getCardsLearnedToday(userId);

        assertEquals(2, cardsLearnedToday); // Assuming only 2 cards learned today
    }
}
