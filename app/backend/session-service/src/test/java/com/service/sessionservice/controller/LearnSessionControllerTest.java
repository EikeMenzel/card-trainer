package com.service.sessionservice.controller;

import com.service.sessionservice.services.AchievementQueryService;
import com.service.sessionservice.services.DbQueryService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = LearnSessionController.class)
@ContextConfiguration(classes = {LearnSessionController.class, DbQueryService.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@WebAppConfiguration
class LearnSessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private DbQueryService dbQueryService;

    @MockBean
    private AchievementQueryService achievementQueryService;

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
    void whenCreateLearnSession_thenReturnOk() throws Exception {
        Pair<HttpStatusCode, Long> mockResponse = Pair.of(HttpStatus.OK, 1L);
        when(dbQueryService.saveLearnSession(anyLong(), anyLong())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/decks/1/learn-sessions")
                        .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void whenGetNextCard_thenReturnOk() throws Exception {
        Pair<HttpStatusCode, Object> mockResponse = Pair.of(HttpStatus.OK, "Card Data");
        when(dbQueryService.getLongestUnseenCard(anyLong(), anyLong(), anyLong())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/decks/1/learn-sessions/1/next-card")
                        .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Card Data"));
    }

    @Test
    void whenUpdateLearnSessionDifficulty_thenReturnNoContent() throws Exception {
        when(dbQueryService.updateLearnSessionDifficulty(anyLong(), anyLong(), any())).thenReturn(HttpStatus.NO_CONTENT);

        mockMvc.perform(put("/api/v1/learn-sessions/1/rating")
                        .header("userId", 1L)
                        .contentType("application/json")
                        .content("{\"ratingLevel\": 3}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenUpdateLearnSessionStatus_thenBadRequest() throws Exception {
        when(dbQueryService.updateLearnSessionStatus(anyLong(), anyLong(), any())).thenReturn(HttpStatus.OK);
        mockMvc.perform(put("/api/v1/learn-sessions/1/status")
                        .header("userId", 1L)
                        .contentType("application/json")
                        .content("{\"statusType\": \"COMPLETED\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreateLearnSession_ReturnsOk() throws Exception {
        when(dbQueryService.saveLearnSession(anyLong(), anyLong())).thenReturn(Pair.of(HttpStatus.OK, 1L));

        mockMvc.perform(post("/api/v1/decks/1/learn-sessions")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void whenCreateLearnSession_ReturnsBadRequest() throws Exception {
        when(dbQueryService.saveLearnSession(anyLong(), anyLong())).thenReturn(Pair.of(HttpStatus.BAD_REQUEST, null));

        mockMvc.perform(post("/api/v1/decks/1/learn-sessions")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetNextCard_ReturnsOk() throws Exception {
        when(dbQueryService.getLongestUnseenCard(anyLong(), anyLong(), anyLong())).thenReturn(Pair.of(HttpStatus.OK, "Card Data"));

        mockMvc.perform(get("/api/v1/decks/1/learn-sessions/1/next-card")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Card Data"));
    }

    @Test
    void whenGetNextCard_ReturnsNoContent() throws Exception {
        when(dbQueryService.getLongestUnseenCard(anyLong(), anyLong(), anyLong())).thenReturn(Pair.of(HttpStatus.NO_CONTENT, null));

        mockMvc.perform(get("/api/v1/decks/1/learn-sessions/1/next-card")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenUpdateLearnSessionDifficulty_ReturnsNoContent() throws Exception {
        when(dbQueryService.updateLearnSessionDifficulty(anyLong(), anyLong(), any())).thenReturn(HttpStatus.NO_CONTENT);

        mockMvc.perform(put("/api/v1/learn-sessions/1/rating")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenUpdateLearnSessionDifficulty_ReturnsBadRequest() throws Exception {
        when(dbQueryService.updateLearnSessionDifficulty(anyLong(), anyLong(), any())).thenReturn(HttpStatus.BAD_REQUEST);

        mockMvc.perform(put("/api/v1/learn-sessions/1/rating")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUpdateLearnSessionStatus_ReturnsBadRequest() throws Exception {
        when(dbQueryService.updateLearnSessionStatus(anyLong(), anyLong(), any())).thenReturn(HttpStatus.OK);

        mockMvc.perform(put("/api/v1/learn-sessions/1/status")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
