package com.service.cardsservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.cardsservice.payload.out.updatecards.CardDTO;
import com.service.cardsservice.payload.out.updatecards.ChoiceAnswerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
class CardsServiceTest {
    @MockBean
    private ObjectMapper objectMapper;

    @MockBean
    private DbQueryService dbQueryService;

    @Autowired
    private CardsService cardsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenSaveCardWithTextAnswer_Success() {
        JsonNode mockCardNode = mock(JsonNode.class);
        Long userId = 1L;
        Long deckId = 1L;

        when(mockCardNode.has("textAnswer")).thenReturn(true);
        when(dbQueryService.saveCardByDeckIdAndUserId(userId, deckId, mockCardNode)).thenReturn(HttpStatus.OK);

        HttpStatusCode result = cardsService.saveCard(mockCardNode, userId, deckId);

        assertEquals(HttpStatus.OK, result);
        verify(dbQueryService, times(1)).saveCardByDeckIdAndUserId(userId, deckId, mockCardNode);
    }
    @Test
    void whenSaveCardWithValidData_Success() {
        JsonNode cardNode = mock(JsonNode.class);
        Long userId = 1L;
        Long deckId = 1L;

        when(cardNode.has("textAnswer")).thenReturn(true);
        when(dbQueryService.saveCardByDeckIdAndUserId(eq(userId), eq(deckId), any(JsonNode.class)))
                .thenReturn(HttpStatus.OK);

        HttpStatusCode response = cardsService.saveCard(cardNode, userId, deckId);

        assertEquals(HttpStatus.OK, response);
        verify(dbQueryService, times(1)).saveCardByDeckIdAndUserId(userId, deckId, cardNode);
    }

    @Test
    void whenSaveCardWithInvalidData_Failure() {
        JsonNode cardNode = mock(JsonNode.class);
        Long userId = 1L;
        Long deckId = 1L;

        when(cardNode.has("textAnswer")).thenReturn(false);
        when(cardNode.has("choiceAnswers")).thenReturn(false);

        HttpStatusCode response = cardsService.saveCard(cardNode, userId, deckId);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response);
    }

    @Test
    void whenUpdateCardWithInvalidData_Failure() throws Exception {
        JsonNode cardNode = mock(JsonNode.class);
        Long userId = 1L;
        Long deckId = 1L;
        Long cardId = 1L;

        when(cardNode.has("textAnswer")).thenReturn(false);
        when(cardNode.has("choiceAnswers")).thenReturn(false);

        HttpStatusCode response = cardsService.updateCard(cardNode, userId, deckId, cardId);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response);
    }

    @Test
    void whenUpdateCardThrowsException_Failure() throws Exception {
        JsonNode cardNode = mock(JsonNode.class);
        Long userId = 1L;
        Long deckId = 1L;
        Long cardId = 1L;

        when(cardNode.has("textAnswer")).thenReturn(true);
        when(objectMapper.treeToValue(eq(cardNode), any(Class.class)))
                .thenThrow(new RuntimeException("Test exception"));

        HttpStatusCode response = cardsService.updateCard(cardNode, userId, deckId, cardId);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response);
    }


}
