package com.service.cardsservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.cardsservice.payload.in.DeckDTO;
import com.service.cardsservice.payload.in.DeckNameDTO;
import com.service.cardsservice.payload.in.HistoryDTO;
import com.service.cardsservice.payload.in.HistoryDetailDTO;
import com.service.cardsservice.payload.in.export.ExportDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
class DbQueryServiceTest {
    @MockBean
    private ObjectMapper objectMapper;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private DbQueryService dbQueryService;

    @Test
    void whenGetCardsAmountByDeck_Success() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("5", HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        Integer result = dbQueryService.getCardsAmountByDeck(1L, 1L);

        assertEquals(5, result);
    }

    @Test
    void whenGetCardsToLearnAmountByDeck_Success() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("3", HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        Integer result = dbQueryService.getCardsToLearnAmountByDeck(1L, 1L);

        assertEquals(3, result);
    }

    @Test
    void whenGetLastLearnedTimestampByDeck_Success() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("2022-01-18T12:34:56Z", HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        Optional<Timestamp> result = dbQueryService.getLastLearnedTimestampByDeck(1L, 1L);

        assertTrue(result.isPresent());
    }

    @Test
    void whenGetLearnStateOfDeck_Success() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("[1,2,3]", HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        List<Integer> result = dbQueryService.getLearnStateOfDeck(1L, 1L);

        Assertions.assertNull(result);
    }

    @Test
    void whenGetAllDecksByUserId_Failure() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("[{}]", HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        List<DeckDTO> result = dbQueryService.getAllDecksByUserId(1L);

        Assertions.assertNull(result);
    }

    @Test
    void whenGetAllDecksByUserId_Success() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("[{}]", HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        List<DeckDTO> result = dbQueryService.getAllDecksByUserId(1L);

        Assertions.assertNull(result);
    }


    @Test
    void whenDeleteDeck_Success() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        when(restTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenReturn(responseEntity);

        HttpStatusCode result = dbQueryService.deleteDeck(1L, 1L);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, result);
    }

    @Test
    void whenGetDeckByUserIdAndDeckId_Success() throws JsonProcessingException {
        DeckDTO deckDTO = new DeckDTO(1L, "X");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(new ObjectMapper().writeValueAsString(deckDTO), HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        Optional<DeckDTO> result = dbQueryService.getDeckByUserIdAndDeckId(1L, 1L);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void whenSaveDeck_Success() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("", HttpStatus.CREATED);
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);

        HttpStatusCode result = dbQueryService.saveDeck(1L, new DeckNameDTO("YASD"));

        Assertions.assertEquals(HttpStatus.CREATED, result);
    }

    @Test
    void whenUpdateDeckInformation_Success() {
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(Void.class))).thenReturn(responseEntity);

        HttpStatusCode result = dbQueryService.updateDeckInformation(1L, 1L, new DeckNameDTO("This is a test"));

        Assertions.assertEquals(HttpStatus.OK, result);
    }

    @Test
    void whenGetCardExportDTOsForExportDeck_Success() throws JsonProcessingException {
        ExportDTO exportDTO = new ExportDTO("X", null);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(new ObjectMapper().writeValueAsString(exportDTO), HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        Optional<ExportDTO> result = dbQueryService.getCardExportDTOsForExportDeck(1L, 1L);

        assertEquals(result, Optional.empty());
    }

    @Test
    void whenGetAllHistoriesByUserIdAndDeckId_Success() throws JsonProcessingException {
        HistoryDTO historyDTO = new HistoryDTO(1L, Timestamp.from(Instant.now()), "FINISHED", 4);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(new ObjectMapper().writeValueAsString(Collections.singletonList(historyDTO)), HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        List<HistoryDTO> result = dbQueryService.getAllHistoriesByUserIdAndDeckId(1L, 1L);

        Assertions.assertNull(result);
    }

    @Test
    void whenGetDetailsHistoryByUserIdAndDeckIdAndHistoryId_Success() throws JsonProcessingException {
        HistoryDetailDTO historyDetailDTO = new HistoryDetailDTO(
                1L, "ABCD", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()),
                1, 2, 3, 4, 5, 6,
                "FINISHED", 4
        );

        ResponseEntity<String> responseEntity = new ResponseEntity<>(new ObjectMapper().writeValueAsString(Optional.of(historyDetailDTO)), HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        Optional<HistoryDetailDTO> result = dbQueryService.getDetailsHistoryByUserIdAndDeckIdAndHistoryId(1L, 1L, 1L);

        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteCard_Success() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        when(restTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenReturn(responseEntity);

        HttpStatusCode result = dbQueryService.deleteCard(1L, 1L, 1L);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, result);
    }

    @Test
    void whenSaveCardByDeckIdAndUserId_Success() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("", HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);

        HttpStatusCode result = dbQueryService.saveCardByDeckIdAndUserId(1L, 1L, new Object());

        Assertions.assertEquals(HttpStatus.OK, result);
    }

    @Test
    void whenSendShareDeckEmail_NotFound() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);

        HttpStatusCode result = dbQueryService.sendShareDeckEmail(1L, "test@example.com", 1L);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    void whenSendShareDeckEmail_Success() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("", HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenReturn(responseEntity);

        HttpStatusCode result = dbQueryService.sendShareDeckEmail(1L, "test@example.com", 1L);

        Assertions.assertNotEquals(HttpStatus.OK, result);
    }

    @Test
    void whenGetCardDetails_Failure() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("testCardDetails", HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenReturn(responseEntity);

        Optional<Object> result = dbQueryService.getCardDetails(1L, 1L, 1L);

        Assertions.assertEquals(result, Optional.empty());
    }

    @Test
    void testUpdateCard() {
        Long userId = 1L;
        Long deckId = 1L;
        Long cardId = 1L;
        Object cardNode = new Object();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Void.class))).thenReturn(responseEntity);

        HttpStatusCode statusCode = dbQueryService.updateCard(userId, deckId, cardId, cardNode);

        assertEquals(HttpStatus.OK, statusCode);
    }

    @Test
    void testSaveImage() {
        Long userId = 1L;
        byte[] imageData = new byte[0];
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = new ResponseEntity<>("1", HttpStatus.CREATED);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(responseEntity);

        Optional<Long> result = dbQueryService.saveImage(userId, imageData);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().longValue());
    }

    @Test
    void testSaveUserLogin() {
        Long userId = 1L;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = new ResponseEntity<>("", HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(responseEntity);

        HttpStatusCode statusCode = dbQueryService.saveUserLogin(userId);

        assertEquals(HttpStatus.OK, statusCode);
    }

}
