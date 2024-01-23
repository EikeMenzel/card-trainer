package com.service.cardsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.cardsservice.payload.out.DeckDetailInformationDTO;
import com.service.cardsservice.payload.out.DeckInformationDTO;
import com.service.cardsservice.payload.in.CardDTO;
import com.service.cardsservice.services.*;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = DeckController.class)
@ContextConfiguration(classes = {DeckController.class, DeckService.class, DeckController.class, AchievementQueryService.class, ImportService.class, ObjectMapper.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@WebAppConfiguration
class DeckControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private DeckService deckService;

    @MockBean
    private DbQueryService dbQueryService;

    @MockBean
    private AchievementQueryService achievementQueryService;

    @MockBean
    private ExportService exportService;

    @MockBean
    private ImportService importService;

    @Autowired
    private DeckController deckController;

    @MockBean
    private ObjectMapper objectMapper;

    @Value("${gateway.path}")
    private String gatewayPath;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }


    @Test
    void whenGetAllDecksByUserId_withValidUserId_thenDecksRetrieved() throws Exception {
        Long userId = 1L;
        List<DeckInformationDTO> deckList = Arrays.asList(new DeckInformationDTO(), new DeckInformationDTO());

        when(deckService.getAllDeckInformation(userId)).thenReturn(deckList);

        mockMvc.perform(get("/api/v1/decks")
                        .header("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void whenDeleteDeck_withValidDeckId_thenDeckDeleted() throws Exception {
        Long userId = 1L;
        Long deckId = 100L;

        when(dbQueryService.deleteDeck(userId, deckId)).thenReturn(HttpStatus.NO_CONTENT);

        mockMvc.perform(delete("/api/v1/decks/{deckId}", deckId)
                        .header("userId", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenGetDetailDeckInformation_withValidDeckId_thenDeckDetailsRetrieved() throws Exception {
        Long userId = 1L;
        Long deckId = 100L;
        DeckDetailInformationDTO deckDetail = new DeckDetailInformationDTO();

        when(deckService.getDetailInformationDeck(userId, deckId)).thenReturn(Optional.of(deckDetail));

        mockMvc.perform(get("/api/v1/decks/{deckId}", deckId)
                        .header("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void whenExportDeck_withInValidDeckId_thenDeckExported() throws Exception {
        Long userId = 1L;
        Long deckId = 100L;
        byte[] zipData = new byte[]{/* ...zip data... */};

        when(exportService.zipDeck(userId, deckId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/decks/{deckId}/export", deckId)
                        .header("userId", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetAllCards_withValidDeckId_thenCardsRetrieved() throws Exception {
        Long userId = 1L;
        Long deckId = 100L;
        List<CardDTO> cards = Arrays.asList(new CardDTO(1L, "X", "BASIC"), new CardDTO(2L, "Y", "BASIC"));
        Pair<List<CardDTO>, HttpStatusCode> pair = Pair.of(cards, HttpStatus.OK);

        when(dbQueryService.getAllCardsByDeckIdAndUserId(userId, deckId)).thenReturn(pair);

        mockMvc.perform(get("/api/v1/decks/{deckId}/cards", deckId)
                        .header("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void whenImportDeck_withValidFile_thenDeckImported() throws Exception {
        Long userId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "deck.zip", "application/zip", /* zip data */ new byte[10]);

        when(importService.processZipFile(any(MultipartFile.class), eq(userId))).thenReturn(HttpStatus.OK);

        mockMvc.perform(multipart("/api/v1/decks/import")
                        .file(file)
                        .header("userId", userId))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetCardsToLearnSize_withValidDeckId_thenSizeReturned() throws Exception {
        Long userId = 1L;
        Long deckId = 100L;
        int size = 5;

        when(dbQueryService.getCardsToLearnAmountByDeck(userId, deckId)).thenReturn(size);

        mockMvc.perform(get("/api/v1/decks/{deckId}/cards-to-learn", deckId)
                        .header("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(size)));
    }

    @Test
    void whenGetAllDecksByUserId_withNoDecks_thenNoContentReturned() throws Exception {
        Long userId = 1L;

        when(deckService.getAllDeckInformation(userId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/decks")
                        .header("userId", userId))
                .andExpect(status().isNoContent());
    }


    @Test
    void whenDeleteDeck_withInvalidDeckId_thenNotFoundReturned() throws Exception {
        Long userId = 1L;
        Long invalidDeckId = -1L;

        when(dbQueryService.deleteDeck(userId, invalidDeckId)).thenReturn(HttpStatus.NOT_FOUND);

        mockMvc.perform(delete("/api/v1/decks/{deckId}", invalidDeckId)
                        .header("userId", userId))
                .andExpect(status().isNotFound());
    }


    @Test
    void whenGetDetailDeckInformation_withNonExistentDeck_thenNotFoundReturned() throws Exception {
        Long userId = 1L;
        Long nonExistentDeckId = 999L;

        when(deckService.getDetailInformationDeck(userId, nonExistentDeckId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/decks/{deckId}", nonExistentDeckId)
                        .header("userId", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenImportDeck_withInvalidFileFormat_thenUnprocessableEntityReturned() throws Exception {
        Long userId = 1L;
        MockMultipartFile invalidFile = new MockMultipartFile("file", "deck.txt", "text/plain", new byte[10]);

        mockMvc.perform(multipart("/api/v1/decks/import")
                        .file(invalidFile)
                        .header("userId", userId))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void whenGetDetailDeckInformation_withValidDeckId_thenDeckDetailsReturned() throws Exception {
        Long userId = 1L;
        Long deckId = 100L;
        DeckDetailInformationDTO deckDetail = new DeckDetailInformationDTO(); // Populate with actual data

        when(deckService.getDetailInformationDeck(userId, deckId)).thenReturn(Optional.of(deckDetail));

        mockMvc.perform(get("/api/v1/decks/{deckId}", deckId)
                        .header("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void whenGetDeckSize_withValidDeckId_thenDeckSizeReturned() throws Exception {
        Long userId = 1L;
        Long deckId = 100L;
        int deckSize = 42; // Example deck size

        when(deckService.getDeckSize(userId, deckId)).thenReturn(deckSize);

        mockMvc.perform(get("/api/v1/decks/{deckId}/size", deckId)
                        .header("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(deckSize)));
    }

    @Test
    void whenGetAllCards_withValidDeckId_thenCardsListReturned() throws Exception {
        Long userId = 1L;
        Long deckId = 100L;
        List<CardDTO> cardList = Arrays.asList(new CardDTO(1L, "Hello?", "BASIC"), new CardDTO(2L, "Test", "MULTIPLE_CHOICE"));
        Pair<List<CardDTO>, HttpStatusCode> pair = Pair.of(cardList, HttpStatus.OK);

        when(dbQueryService.getAllCardsByDeckIdAndUserId(userId, deckId)).thenReturn(pair);

        mockMvc.perform(get("/api/v1/decks/{deckId}/cards", deckId)
                        .header("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void whenGetCardsToLearnSize_withValidDeckId_thenLearnableCardsCountReturned() throws Exception {
        Long userId = 1L;
        Long deckId = 100L;
        int learnableCardsCount = 10; // Example count

        when(dbQueryService.getCardsToLearnAmountByDeck(userId, deckId)).thenReturn(learnableCardsCount);

        mockMvc.perform(get("/api/v1/decks/{deckId}/cards-to-learn", deckId)
                        .header("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(learnableCardsCount)));
    }


    @Test
    void whenCopySharedDeck_withInvalidToken_thenBadRequestReturned() throws Exception {
        String invalidToken = "short";

        mockMvc.perform(get("/api/v1/decks/share/{token}", invalidToken))
                .andExpect(status().isBadRequest());
    }

}
