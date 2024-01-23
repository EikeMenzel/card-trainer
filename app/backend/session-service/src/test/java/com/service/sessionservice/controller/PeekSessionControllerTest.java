package com.service.sessionservice.controller;

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
@WebMvcTest(controllers = PeekSessionController.class)
@ContextConfiguration(classes = {PeekSessionController.class, DbQueryService.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@WebAppConfiguration
class PeekSessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private DbQueryService dbQueryService;
    @InjectMocks
    private PeekSessionController peekSessionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void whenCreatePeekSession_ReturnsOk() throws Exception {
        when(dbQueryService.savePeekSession(anyLong(), anyLong())).thenReturn(Pair.of(HttpStatus.OK, 1L));

        mockMvc.perform(post("/api/v1/decks/1/peek-sessions")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void whenCreatePeekSession_ReturnsBadRequest() throws Exception {
        when(dbQueryService.savePeekSession(anyLong(), anyLong())).thenReturn(Pair.of(HttpStatus.BAD_REQUEST, 0L));

        mockMvc.perform(post("/api/v1/decks/1/peek-sessions")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreatePeekSession_ReturnsNotFound() throws Exception {
        when(dbQueryService.savePeekSession(anyLong(), anyLong())).thenReturn(Pair.of(HttpStatus.NOT_FOUND, 0L));

        mockMvc.perform(post("/api/v1/decks/1/peek-sessions")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCreatePeekSession_ReturnsInternalServerError() throws Exception {
        when(dbQueryService.savePeekSession(anyLong(), anyLong())).thenReturn(Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, 0L));

        mockMvc.perform(post("/api/v1/decks/1/peek-sessions")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenGetNextCard_ReturnsOk() throws Exception {
        when(dbQueryService.getRandomPeekSessionCard(anyLong(), anyLong())).thenReturn(Pair.of(HttpStatus.OK, "Card Data"));

        mockMvc.perform(get("/api/v1/peek-sessions/1/next-card")
                        .header("userId", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Card Data"));
    }

    @Test
    void whenGetNextCard_ReturnsNoContent() throws Exception {
        when(dbQueryService.getRandomPeekSessionCard(anyLong(), anyLong())).thenReturn(Pair.of(HttpStatus.NO_CONTENT, null));

        mockMvc.perform(get("/api/v1/peek-sessions/1/next-card")
                        .header("userId", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenGetNextCard_ReturnsBadRequest() throws Exception {
        when(dbQueryService.getRandomPeekSessionCard(anyLong(), anyLong())).thenReturn(Pair.of(HttpStatus.BAD_REQUEST, null));

        mockMvc.perform(get("/api/v1/peek-sessions/1/next-card")
                        .header("userId", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetNextCard_ReturnsInternalServerError() throws Exception {
        when(dbQueryService.getRandomPeekSessionCard(anyLong(), anyLong())).thenReturn(Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, null));

        mockMvc.perform(get("/api/v1/peek-sessions/1/next-card")
                        .header("userId", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenSavePeekSessionCard_ReturnsNoContent() throws Exception {
        when(dbQueryService.savePeekSessionCard(anyLong(), anyLong(), anyLong())).thenReturn(HttpStatus.NO_CONTENT);

        mockMvc.perform(post("/api/v1/peek-sessions/1/cards/2")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenSavePeekSessionCard_ReturnsBadRequest() throws Exception {
        when(dbQueryService.savePeekSessionCard(anyLong(), anyLong(), anyLong())).thenReturn(HttpStatus.BAD_REQUEST);

        mockMvc.perform(post("/api/v1/peek-sessions/1/cards/2")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenSavePeekSessionCard_ReturnsNotFound() throws Exception {
        when(dbQueryService.savePeekSessionCard(anyLong(), anyLong(), anyLong())).thenReturn(HttpStatus.NOT_FOUND);

        mockMvc.perform(post("/api/v1/peek-sessions/1/cards/2")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenSavePeekSessionCard_ReturnsInternalServerError() throws Exception {
        when(dbQueryService.savePeekSessionCard(anyLong(), anyLong(), anyLong())).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

        mockMvc.perform(post("/api/v1/peek-sessions/1/cards/2")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenUpdatePeekSessionStatus_ReturnsBadRequest() throws Exception {
        when(dbQueryService.updatePeekSessionStatus(anyLong(), anyLong(), any())).thenReturn(HttpStatus.BAD_REQUEST);

        mockMvc.perform(put("/api/v1/peek-sessions/1/status")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
