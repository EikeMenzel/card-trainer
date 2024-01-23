package com.service.cardsservice.controller;

import com.service.cardsservice.payload.in.HistoryDTO;
import com.service.cardsservice.payload.in.HistoryDetailDTO;
import com.service.cardsservice.services.DbQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HistoryController.class)
@ContextConfiguration(classes = {HistoryController.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@WebAppConfiguration
class HistoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private DbQueryService dbQueryService;

    @Autowired
    private HistoryController historyController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void whenGetAllHistoriesByUserIdAndDeckId_thenReturnHistories() throws Exception {
        long userId = 1L;
        long deckId = 100L;

        List<HistoryDTO> historyList = Arrays.asList(new HistoryDTO(1L, Timestamp.from(Instant.now()), "FINISHED", 5), new HistoryDTO(2L, Timestamp.from(Instant.now()), "FINISHED", 5));

        when(dbQueryService.getAllHistoriesByUserIdAndDeckId(userId, deckId)).thenReturn(historyList);

        mockMvc.perform(get("/api/v1/decks/{deckId}/histories", deckId)
                        .header("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void whenGetHistoryDetailsByUserIdAndHistoryId_thenReturnHistoryDetail() throws Exception {
        long userId = 1L;
        long deckId = 100L;
        long historyId = 10L;

        HistoryDetailDTO historyDetailDTO = new HistoryDetailDTO(
                1L,          // historyId
                "Sample Deck",          // deckName
                Timestamp.valueOf("2022-01-01 12:00:00"), // createdAt
                Timestamp.valueOf("2022-01-01 13:30:00"), // finishedAt
                2,           // difficulty_1
                5,           // difficulty_2
                7,           // difficulty_3
                3,           // difficulty_4
                6,           // difficulty_5
                8,           // difficulty_6
                "Completed", // status
                25           // cardsLearned
        );

        when(dbQueryService.getDetailsHistoryByUserIdAndDeckIdAndHistoryId(userId, deckId, historyId))
                .thenReturn(Optional.of(historyDetailDTO));

        mockMvc.perform(get("/api/v1/decks/{deckId}/histories/{historyId}", deckId, historyId)
                        .header("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    void testGetAllHistoriesByUserIdAndDeckId_NoContent() {
        Long userId = 1L;
        Long deckId = 1L;

        when(dbQueryService.getAllHistoriesByUserIdAndDeckId(userId, deckId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<HistoryDTO>> response = historyController.getAllHistoriesByUserIdAndDeckId(userId, deckId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void whenGetAllHistoriesByUserIdAndDeckId_withNoHistories_thenReturnNoContent() throws Exception {
        long userId = 1L;
        long deckId = 100L;

        when(dbQueryService.getAllHistoriesByUserIdAndDeckId(userId, deckId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/decks/{deckId}/histories", deckId)
                        .header("userId", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenGetHistoryDetailsByUserIdAndHistoryId_withHistoryNotFound_thenReturnNotFound() throws Exception {
        long userId = 1L;
        long deckId = 100L;
        long historyId = 10L;

        when(dbQueryService.getDetailsHistoryByUserIdAndDeckIdAndHistoryId(userId, deckId, historyId))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/decks/{deckId}/histories/{historyId}", deckId, historyId)
                        .header("userId", userId))
                .andExpect(status().isNotFound());
    }
}
