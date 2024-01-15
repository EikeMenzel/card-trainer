package com.service.databaseservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.databaseservice.payload.inc.learnsession.RatingCardHandlerDTO;
import com.service.databaseservice.payload.inc.learnsession.RatingLevelDTO;
import com.service.databaseservice.payload.inc.learnsession.StatusTypeDTO;
import com.service.databaseservice.payload.out.HistoryDTO;
import com.service.databaseservice.payload.out.HistoryDetailDTO;
import com.service.databaseservice.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = LearnSessionController.class)
@ContextConfiguration(classes = {LearnSessionController.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@WebAppConfiguration
class LearnSessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private LearnSessionService learnSessionService;

    @MockBean
    private DeckService deckService;

    @MockBean
    private CardService cardService;

    @MockBean
    private RepetitionService repetitionService;

    @InjectMocks
    private LearnSessionController learnSessionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void whenGetLastLearnedTimestamp_WithValidUserIdAndDeckId_thenReturnsOk() throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        when(learnSessionService.getLastLearnedFromLearnSessionById(anyLong(), anyLong())).thenReturn(Optional.of(timestamp));

        mockMvc.perform(get("/api/v1/db/users/1/learn-sessions/2/timestamp")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetLastLearnedTimestamp_SessionOrUserNotFound_thenReturnsNotFound() throws Exception {
        when(learnSessionService.getLastLearnedFromLearnSessionById(anyLong(), anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/db/users/1/learn-sessions/2/timestamp")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetAllLearnSessions_WithValidUserIdAndDeckId_thenReturnsOk() throws Exception {
        List<HistoryDTO> historyDTOs = Collections.singletonList(new HistoryDTO(1L, new Timestamp(13222222), "FINISHED", 10));
        when(learnSessionService.getAllHistoryFromUserIdAndDeckId(anyLong(), anyLong())).thenReturn(historyDTOs);

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/histories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetAllLearnSessions_NoSessionsOrUserFound_thenReturnsNoContent() throws Exception {
        when(learnSessionService.getAllHistoryFromUserIdAndDeckId(anyLong(), anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/histories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenCreateLearnSession_WithValidUserIdAndDeckId_thenReturnsOk() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(true);
        when(learnSessionService.createLearnSession(anyLong(), anyLong())).thenReturn(Optional.of(1L));

        mockMvc.perform(post("/api/v1/db/users/1/decks/2/learn-sessions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenCreateLearnSession_DeckOrUserNotFound_thenReturnsNotFound() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(post("/api/v1/db/users/1/decks/2/learn-sessions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenUpdateLearnSessionRating_WithValidData_thenReturnsNoContent() throws Exception {
        when(learnSessionService.doesLearnSessionFromUserExist(anyLong(), anyLong())).thenReturn(true);
        when(cardService.doesCardBelongToOwner(anyLong(), anyLong())).thenReturn(true);
        when(learnSessionService.updateRatingInLearnSession(anyLong(), any())).thenReturn(true);
        when(repetitionService.updateRepetition(anyLong(), any())).thenReturn(true);

        mockMvc.perform(put("/api/v1/db/users/1/learn-sessions/2/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(new RatingCardHandlerDTO(3L, RatingLevelDTO.RATING_1))))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenUpdateLearnSessionRating_SessionOrCardOrUserNotFound_thenReturnsNotFound() throws Exception {
        when(learnSessionService.doesLearnSessionFromUserExist(anyLong(), anyLong())).thenReturn(false);
        when(cardService.doesCardBelongToOwner(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(put("/api/v1/db/users/1/learn-sessions/2/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(new RatingCardHandlerDTO(3L, RatingLevelDTO.RATING_1))))
                .andExpect(status().isNotFound());
    }


    private String convertToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    void whenUpdateLearnSessionStatus_WithValidData_thenReturnsNoContent() throws Exception {
        when(learnSessionService.doesLearnSessionFromUserExist(anyLong(), anyLong())).thenReturn(true);
        when(learnSessionService.updateStatusTypeInLearnSession(anyLong(), any())).thenReturn(true);

        mockMvc.perform(put("/api/v1/db/users/1/learn-sessions/2/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(StatusTypeDTO.CANCELED)))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenUpdateLearnSessionStatus_SessionOrUserNotFound_thenReturnsNotFound() throws Exception {
        when(learnSessionService.doesLearnSessionFromUserExist(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(put("/api/v1/db/users/1/learn-sessions/2/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(StatusTypeDTO.FINISHED)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetLongestUnseenCard_NoCardsFound_thenReturnsNoContent() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(true);
        when(cardService.getOldestCardToLearn(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/cards/longest-unseen")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenGetLearnSessionCount_WithValidUserId_thenReturnsOk() throws Exception {
        when(learnSessionService.getLearnSessionCount(anyLong())).thenReturn(10);

        mockMvc.perform(get("/api/v1/db/users/1/learn-sessions/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetCardsLearnedAmount_WithValidUserId_thenReturnsOk() throws Exception {
        when(learnSessionService.getCardsLearnedCount(anyLong())).thenReturn(15);

        mockMvc.perform(get("/api/v1/db/users/1/cards-learned")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetDailyLearnSession_WithValidUserId_thenReturnsOk() throws Exception {
        when(learnSessionService.isDailyLearnSessionCompletedToday(anyLong())).thenReturn(true);

        mockMvc.perform(get("/api/v1/db/users/1/learn-sessions/daily")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetCardsLearnedAmountDaily_WithValidUserId_thenReturnsOk() throws Exception {
        when(learnSessionService.getCardsLearnedToday(anyLong())).thenReturn(5);

        mockMvc.perform(get("/api/v1/db/users/1/learn-sessions/cards-learned/daily")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetLearnSessionDetails_WithValidData_thenReturnsOk() throws Exception {
        Timestamp exampleCreatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp exampleFinishedAt = new Timestamp(System.currentTimeMillis() + 3600000); // 1 hour later

        HistoryDetailDTO historyDetailDTO = new HistoryDetailDTO(
                123L,                         // historyId
                "Example Deck",               // deckName
                exampleCreatedAt,             // createdAt
                exampleFinishedAt,            // finishedAt
                5,                            // difficulty_1
                10,                           // difficulty_2
                8,                            // difficulty_3
                3,                            // difficulty_4
                0,                            // difficulty_5
                1,                            // difficulty_6
                "COMPLETED",                  // status
                27                            // cardsLearned
        );

        when(learnSessionService.getHistoryDetailsFromHistoryIdAndUserId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(historyDetailDTO));

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/histories/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetLearnSessionDetails_SessionOrUserNotFound_thenReturnsNotFound() throws Exception {
        when(learnSessionService.getHistoryDetailsFromHistoryIdAndUserId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/histories/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
