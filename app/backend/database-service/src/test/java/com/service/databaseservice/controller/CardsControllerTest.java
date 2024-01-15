package com.service.databaseservice.controller;

import com.service.databaseservice.payload.out.CardDTO;
import com.service.databaseservice.services.CardService;
import com.service.databaseservice.services.DeckService;
import com.service.databaseservice.services.LearnSessionService;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CardsController.class)
@ContextConfiguration(classes = {CardsController.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@WebAppConfiguration
class CardsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private DeckService deckService;

    @MockBean
    private CardService cardService;
    @InjectMocks
    private CardsController cardsController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void whenGetAllCardsByDeck_ValidData_thenReturnsOk() throws Exception {
        List<CardDTO> cardDTOList = Arrays.asList(new CardDTO(1L, "What is the capital of France?", "Geography"),
        new CardDTO(2L, "Explain the theory of relativity.", "Physics"));
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(true);
        when(cardService.getCardsFromDeckId(anyLong())).thenReturn(cardDTOList);

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/cards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetAllCardsByDeck_NoCards_thenReturnsNoContent() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(true);
        when(cardService.getCardsFromDeckId(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/cards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenGetAllCardsByDeck_DeckOrUserNotFound_thenReturnsNotFound() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/cards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteCard_ValidData_thenReturnsNoContent() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(true);
        when(cardService.deleteCard(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/db/users/1/decks/2/cards/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteCard_CardOrDeckOrUserNotFound_thenReturnsNotFound() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(delete("/api/v1/db/users/1/decks/2/cards/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenSaveCard_ValidData_thenReturnsCreated() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(true);
        when(cardService.saveCard(any(), anyLong(), anyLong())).thenReturn(true);

        mockMvc.perform(post("/api/v1/db/users/1/decks/2/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cardData\": \"example data\" }")) // Replace with actual JSON structure
                .andExpect(status().isCreated());
    }

    @Test
    void whenSaveCard_DeckOrUserNotFound_thenReturnsNotFound() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(post("/api/v1/db/users/1/decks/2/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cardData\": \"example data\" }")) // Replace with actual JSON structure
                .andExpect(status().isNotFound());
    }

    @Test
    void whenUpdateCard_ValidData_thenReturnsNoContent() throws Exception {
        when(cardService.doesCardBelongToOwnerAndDeck(anyLong(), anyLong(), anyLong())).thenReturn(true);
        when(cardService.updateCard(any(), anyLong(), anyLong())).thenReturn(true);

        mockMvc.perform(put("/api/v1/db/users/1/decks/2/cards/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"updatedCardData\": \"new data\" }")) // Replace with actual JSON structure
                .andExpect(status().isNoContent());
    }

    @Test
    void whenUpdateCard_CardOrDeckOrUserNotFound_thenReturnsNotFound() throws Exception {
        when(cardService.doesCardBelongToOwnerAndDeck(anyLong(), anyLong(), anyLong())).thenReturn(false);
        mockMvc.perform(put("/api/v1/db/users/1/decks/2/cards/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"updatedCardData\": \"new data\" }")) // Replace with actual JSON structure
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetCardDetails_CardOrDeckNotFound_thenReturnsNotFound() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/cards/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
