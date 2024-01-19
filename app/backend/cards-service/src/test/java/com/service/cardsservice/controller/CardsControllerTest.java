package com.service.cardsservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.cardsservice.services.CardsService;
import com.service.cardsservice.services.DbQueryService;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CardsController.class)
@ContextConfiguration(classes = {CardsController.class, CardsService.class, DbQueryService.class, ObjectMapper.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@WebAppConfiguration
class CardsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private DbQueryService dbQueryService;

    @MockBean
    private ObjectMapper objectMapper;

    @InjectMocks
    private CardsService cardsService;

    @Autowired
    private CardsController cardsController;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void whenSaveCard_withInValidData_thenCardSaved() throws Exception {
        long userId = 1L;
        long deckId = 100L;
        String cardData = "{\"name\":\"Test Card\", \"description\":\"Test Description\"}";

        mockMvc.perform(post("/api/v1/decks/{deckId}/cards", deckId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cardData))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void whenUpdateCard_withInValidData_thenCardUpdated() throws Exception {
        long userId = 1L;
        long deckId = 100L;
        long cardId = 10L;
        String updatedCardData = "{\"name\":\"Updated Card\", \"description\":\"Updated Description\"}";

        when(dbQueryService.updateCard(any(), any(), any(), any())).thenReturn(HttpStatus.NO_CONTENT);

        mockMvc.perform(put("/api/v1/decks/{deckId}/cards/{cardId}", deckId, cardId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedCardData))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void whenDeleteCard_withValidData_thenCardDeleted() throws Exception {
        long userId = 1L;
        long deckId = 100L;
        long cardId = 10L;

        when(dbQueryService.deleteCard(userId, deckId, cardId)).thenReturn(HttpStatus.NO_CONTENT);

        mockMvc.perform(delete("/api/v1/decks/{deckId}/cards/{cardId}", deckId, cardId)
                        .header("userId", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenSaveCard_withInvalidJson_thenUnprocessableEntity() throws Exception {
        long userId = 1L;
        long deckId = 100L;
        String invalidCardData = "invalid json";

        mockMvc.perform(post("/api/v1/decks/{deckId}/cards", deckId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCardData))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void whenGetDetailCardInformation_withInvalidCardId_thenNotFound() throws Exception {
        long userId = 1L;
        long deckId = 100L;
        long invalidCardId = -1L;

        mockMvc.perform(get("/api/v1/decks/{deckId}/cards/{cardId}", deckId, invalidCardId)
                        .header("userId", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteCard_withUnauthorizedUser_thenUnauthorized() throws Exception {
        long unauthorizedUserId = 99L;
        long deckId = 100L;
        long cardId = 10L;

        // Mock behavior assuming unauthorized access
        when(dbQueryService.deleteCard(unauthorizedUserId, deckId, cardId)).thenReturn(HttpStatus.UNAUTHORIZED);

        mockMvc.perform(delete("/api/v1/decks/{deckId}/cards/{cardId}", deckId, cardId)
                        .header("userId", unauthorizedUserId))
                .andExpect(status().isUnauthorized());
    }

}
