package com.service.databaseservice.controller;

import com.service.databaseservice.payload.inc.learnsession.StatusTypeDTO;
import com.service.databaseservice.services.CardService;
import com.service.databaseservice.services.DeckService;
import com.service.databaseservice.services.PeekSessionService;
import com.service.databaseservice.services.UserAchievementService;
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

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PeekSessionController.class)
@ContextConfiguration(classes = {PeekSessionController.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@WebAppConfiguration
class PeekSessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private CardService cardService;

    @MockBean
    private DeckService deckService;

    @MockBean
    private PeekSessionService peekSessionService;

    @InjectMocks
    private PeekSessionController peekSessionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(true);
        when(peekSessionService.createPeekSession(anyLong(), anyLong())).thenReturn(Optional.of(1L));
        when(peekSessionService.doesPeekSessionFromUserExist(anyLong(), anyLong())).thenReturn(true);
        when(cardService.doesCardBelongToOwner(anyLong(), anyLong())).thenReturn(true);
        when(peekSessionService.savePeekSessionCard(anyLong(), anyLong(), anyLong())).thenReturn(true);
        when(peekSessionService.updateStatusTypeInPeekSession(anyLong(), any())).thenReturn(true);
        when(peekSessionService.getRandomCardToLearn(anyLong())).thenReturn(Optional.of(new Object()));
    }

    @Test
    void whenCreatePeekSession_WithValidData_thenReturnsOk() throws Exception {
        mockMvc.perform(post("/api/v1/db/users/1/decks/2/peek-sessions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenSaveCardInPeekSession_WithValidData_thenReturnsNoContent() throws Exception {
        mockMvc.perform(post("/api/v1/db/users/1/peek-sessions/2/cards/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenCreatePeekSession_WithInvalidDeckOrUserId_thenReturnsNotFound() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(post("/api/v1/db/users/1/decks/2/peek-sessions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCreatePeekSession_ServiceFailure_thenReturnsInternalServerError() throws Exception {
        when(peekSessionService.createPeekSession(anyLong(), anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/db/users/1/decks/2/peek-sessions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenUpdatePeekSessionStatus_PeekSessionNotFound_thenReturnsBadRequest() throws Exception {
        when(peekSessionService.doesPeekSessionFromUserExist(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(put("/api/v1/db/users/1/peek-sessions/2/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"status\": \"new_status\" }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenSaveCardInPeekSession_CardOrPeekSessionNotFound_thenReturnsNotFound() throws Exception {
        when(peekSessionService.doesPeekSessionFromUserExist(anyLong(), anyLong())).thenReturn(false);
        when(cardService.doesCardBelongToOwner(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(post("/api/v1/db/users/1/peek-sessions/2/cards/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenSaveCardInPeekSession_ServiceFailure_thenReturnsInternalServerError() throws Exception {
        when(peekSessionService.savePeekSessionCard(anyLong(), anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(post("/api/v1/db/users/1/peek-sessions/2/cards/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenGetRandomCardFromPeekSession_PeekSessionNotFound_thenReturnsNotFound() throws Exception {
        when(peekSessionService.doesPeekSessionFromUserExist(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(get("/api/v1/db/users/1/peek-sessions/2/cards/random-card")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetRandomCardFromPeekSession_NoCardsAvailable_thenReturnsNoContent() throws Exception {
        when(peekSessionService.getRandomCardToLearn(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/db/users/1/peek-sessions/2/cards/random-card")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}