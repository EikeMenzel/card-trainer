package com.service.databaseservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.RepetitionModel;

import com.service.databaseservice.model.User;
import com.service.databaseservice.payload.inc.DeckNameDTO;
import com.service.databaseservice.payload.out.DeckDTO;
import com.service.databaseservice.payload.out.export.CardExportDTO;
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

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = DeckController.class)
@ContextConfiguration(classes = {DeckController.class, DeckService.class, CardService.class, RepetitionService.class, ExportService.class,
        ObjectMapper.class, UserTokenService.class, DeckController.class})
@TestPropertySource(locations = "classpath:application.properties")
@WebAppConfiguration
class DeckControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private DeckService deckService;

    @MockBean
    private CardService cardService;

    @MockBean
    private RepetitionService repetitionService;

    @MockBean
    private ExportService exportService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserTokenService userTokenService;

    @MockBean
    private ImageService imageService;
    @InjectMocks
    private DeckController deckController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }
    @Test
    void whenGetAllDecksByUser_ValidUserId_thenReturnsOk() throws Exception {
        List<DeckDTO> deckDTOs = Arrays.asList(new DeckDTO(1L, "Test"), new DeckDTO(2L, "XY"));
        when(deckService.getAllDecksByUserId(anyLong())).thenReturn(deckDTOs);

        mockMvc.perform(get("/api/v1/db/users/1/decks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetAllDecksByUser_NoDecksFound_thenReturnsNoContent() throws Exception {
        when(deckService.getAllDecksByUserId(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/db/users/1/decks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenGetDeckByIdAndUserId_ValidData_thenReturnsOk() throws Exception {
        Optional<DeckDTO> deckDTO = Optional.of(new DeckDTO(1L, "Test"));
        when(deckService.getDeckByIdAndUserId(anyLong(), anyLong())).thenReturn(deckDTO);

        mockMvc.perform(get("/api/v1/db/users/1/decks/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetDeckByIdAndUserId_DeckNotFound_thenReturnsNoContent() throws Exception {
        when(deckService.getDeckByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/db/users/1/decks/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteDeck_ValidData_thenReturnsNoContent() throws Exception {
        when(deckService.deleteDeck(anyLong(), anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/db/users/1/decks/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteDeck_DeckOrUserNotFound_thenReturnsNotFound() throws Exception {
        when(deckService.deleteDeck(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(delete("/api/v1/db/users/1/decks/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetDeckCount_ValidUserId_thenReturnsOk() throws Exception {
        when(deckService.getDeckCountByUserId(anyLong())).thenReturn(5);

        mockMvc.perform(get("/api/v1/db/users/1/decks/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetDeckName_ValidDeckId_thenReturnsOk() throws Exception {
        Optional<String> deckName = Optional.of("Deck Name");
        when(deckService.getDeckNameById(anyLong())).thenReturn(deckName);

        mockMvc.perform(get("/api/v1/db/decks/1/name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetDeckName_DeckNotFound_thenReturnsNoContent() throws Exception {
        when(deckService.getDeckNameById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/db/decks/1/name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenGetAmountOfCardsInDeck_ValidData_thenReturnsOk() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(true);
        when(cardService.getCardAmountFromDeckId(anyLong())).thenReturn(10);

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/cards/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetAmountOfCardsInDeck_DeckOrUserNotFound_thenReturnsNotFound() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/cards/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetCardsToLearnAmount_ValidData_thenReturnsOk() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(true);
        when(cardService.getCardsByDeckId(anyLong())).thenReturn(new ArrayList<>());
        when(repetitionService.getRepetitionByCardId(anyLong())).thenReturn(Optional.of(new RepetitionModel()));

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/cards-to-learn/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetCardsToLearnAmount_DeckOrUserNotFound_thenReturnsNotFound() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/cards-to-learn/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCountCardValues_ValidData_thenReturnsOk() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(true);
        when(cardService.getCardsByDeckId(anyLong())).thenReturn(new ArrayList<>());
        when(repetitionService.getRepetitionByCardId(anyLong())).thenReturn(Optional.of(new RepetitionModel()));

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/learn-state")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenCountCardValues_DeckOrUserNotFound_thenReturnsNotFound() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/learn-state")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenExistsDeckByUserIdAndDeckId_DeckExists_thenReturnsNoContent() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(true);

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/exists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenExistsDeckByUserIdAndDeckId_DeckNotFound_thenReturnsNotFound() throws Exception {
        when(deckService.existsByDeckIdAndUserId(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/exists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCloneSharedDeck_InvalidToken_thenReturnsNotFound() throws Exception {
        when(userTokenService.getUserByUserToken(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/db/decks/share/token123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenExportCardsFromDeck_ValidData_thenReturnsOk() throws Exception {
        when(deckService.getDeckNameByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of("Deck Name"));
        when(exportService.getDeckForExport(anyLong(), anyLong())).thenReturn(Optional.of(Collections.singletonList(new CardExportDTO())));

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/cards/export")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenExportCardsFromDeck_DeckOrUserNotFound_thenReturnsNotFound() throws Exception {
        when(deckService.getDeckNameByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/db/users/1/decks/2/cards/export")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenImportDeck_ValidData_thenReturnsCreated() throws Exception {
        when(deckService.createDeck(anyLong(), any(DeckNameDTO.class))).thenReturn(true);
        when(deckService.findTopByOwnerIdAndNameOrderByIdDesc(anyLong(), anyString())).thenReturn(Optional.of(new Deck()));

        mockMvc.perform(post("/api/v1/db/users/1/decks/import")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"deckName\":\"Imported Deck\", \"cardExportDTOList\":[]}"))
                .andExpect(status().isCreated());
    }

    @Test
    void whenImportDeck_DeckCreationFails_thenReturnsInternalServerError() throws Exception {
        when(deckService.createDeck(anyLong(), any(DeckNameDTO.class))).thenReturn(false);

        mockMvc.perform(post("/api/v1/db/users/1/decks/import")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"deckName\":\"Imported Deck\", \"cardExportDTOList\":[]}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCloneSharedDeck_DeckIdNotFound_thenReturnsNotFound() throws Exception {
        String token = "validToken";
        when(userTokenService.getUserByUserToken(token)).thenReturn(Optional.of(new User()));
        when(userTokenService.isUserTokenValid(token)).thenReturn(true);
        when(userTokenService.areTokenTypesIdentical(token, "SHARE_DECK")).thenReturn(true);
        when(userTokenService.getDeckIdByUserToken(token)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/db/decks/share/" + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
