package com.service.sessionservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.sessionservice.payload.RatingCardHandlerDTO;
import com.service.sessionservice.payload.RatingLevelDTO;
import com.service.sessionservice.payload.StatusTypeDTO;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
class DbQueryServiceTest {
    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private DbQueryService dbQueryService;

    @Test
    void whenSaveLearnSession_Success() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("1", HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);

        Pair<HttpStatusCode, Long> result = dbQueryService.saveLearnSession(1L, 1L);

        assertEquals(HttpStatus.OK, result.getLeft());
        assertEquals(1L, result.getRight());
    }

    @Test
    void whenSaveLearnSession_HandlesException() {
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Pair<HttpStatusCode, Long> result = dbQueryService.saveLearnSession(1L, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, result.getLeft());
        assertNull(result.getRight());
    }
    @Test
    void whenUpdateLearnSessionDifficulty_Success() {
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(Void.class)))
                .thenReturn(responseEntity);

        HttpStatusCode result = dbQueryService.updateLearnSessionDifficulty(1L, 1L, new RatingCardHandlerDTO(1l, RatingLevelDTO.RATING_0));

        assertEquals(HttpStatus.OK, result);
    }

    @Test
    void whenUpdateLearnSessionDifficulty_HandlesException() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        HttpStatusCode result = dbQueryService.updateLearnSessionDifficulty(1L, 1L, new RatingCardHandlerDTO(1L, RatingLevelDTO.RATING_0));

        assertEquals(HttpStatus.BAD_REQUEST, result);
    }


    @Test
    void whenGetLongestUnseenCard_Success() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Card Data", HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        Pair<HttpStatusCode, Object> result = dbQueryService.getLongestUnseenCard(1L, 2L, 3L);

        assertEquals(HttpStatus.OK, result.getLeft());
        assertNotNull(result.getRight());
        assertNotEquals(Optional.empty(), result.getRight());
    }

    @Test
    void whenSavePeekSession_Success() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("1", HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);

        Pair<HttpStatusCode, Long> result = dbQueryService.savePeekSession(1L, 1L);

        assertEquals(HttpStatus.OK, result.getLeft());
        assertEquals(1L, result.getRight());
    }

    @Test
    void whenSavePeekSessionCard_Success() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);

        HttpStatusCode result = dbQueryService.savePeekSessionCard(1L, 1L, 1L);

        assertEquals(HttpStatus.OK, result);
    }

    @Test
    void whenUpdatePeekSessionStatus_Success() {
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(Void.class))).thenReturn(responseEntity);

        HttpStatusCode result = dbQueryService.updatePeekSessionStatus(1L, 1L, StatusTypeDTO.FINISHED);

        assertEquals(HttpStatus.OK, result);
    }

    @Test
    void whenGetRandomPeekSessionCard_Success() {
        String mockResponse = "Card Data";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        Pair<HttpStatusCode, Object> result = dbQueryService.getRandomPeekSessionCard(1L, 1L);

        assertEquals(HttpStatus.OK, result.getLeft());
        assertNotNull(result.getRight());
    }

    @Test
    void whenSavePeekSession_HandlesException() {
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Pair<HttpStatusCode, Long> result = dbQueryService.savePeekSession(1L, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, result.getLeft());
        assertNull(result.getRight());
    }

    @Test
    void whenSavePeekSessionCard_HandlesException() {
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        HttpStatusCode result = dbQueryService.savePeekSessionCard(1L, 1L, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    void whenUpdateLearnSessionStatus_HandlesException() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        HttpStatusCode result = dbQueryService.updateLearnSessionStatus(1L, 1L, StatusTypeDTO.FINISHED);

        assertEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    void whenUpdatePeekSessionStatus_HandlesException() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        HttpStatusCode result = dbQueryService.updatePeekSessionStatus(1L, 1L, StatusTypeDTO.CANCELED);

        assertEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    void whenGetRandomPeekSessionCard_HttpClientErrorException() {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenThrow(exception);

        Pair<HttpStatusCode, Object> result = dbQueryService.getRandomPeekSessionCard(1L, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, result.getLeft());
        assertNotNull(result.getRight());
    }

    @Test
    void whenGetRandomPeekSessionCard_HttpServerErrorException() {
        HttpServerErrorException exception = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenThrow(exception);

        Pair<HttpStatusCode, Object> result = dbQueryService.getRandomPeekSessionCard(1L, 1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getLeft());
        assertEquals(result.getRight(), Optional.empty());
    }


    @Test
    void whenGetLongestUnseenCard_thenReturnsNoContent() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        Pair<HttpStatusCode, Object> result = dbQueryService.getLongestUnseenCard(1L, 2L, 3L);

        assertEquals(HttpStatus.NO_CONTENT, result.getLeft());
        assertNotNull(result.getRight());
    }

    @Test
    void whenGetLongestUnseenCard_thenReturnsSameStatus() {
        HttpClientErrorException exception = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found", new HttpHeaders(), null, null);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenThrow(exception);

        Pair<HttpStatusCode, Object> result = dbQueryService.getLongestUnseenCard(1L, 2L, 3L);

        assertEquals(HttpStatus.NOT_FOUND, result.getLeft());
        assertEquals(Optional.empty(), result.getRight());
    }

    @Test
    void whenGetLongestUnseenCard_thenReturnsConflict() {
        HttpClientErrorException exception = HttpClientErrorException.create(HttpStatus.CONFLICT, "Conflict", new HttpHeaders(), "Error Message".getBytes(), null);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenThrow(exception);

        Pair<HttpStatusCode, Object> result = dbQueryService.getLongestUnseenCard(1L, 2L, 3L);

        assertEquals(HttpStatus.CONFLICT, result.getLeft());
        assertNotNull(result.getRight());
        assertNotEquals(Optional.empty(), result.getRight());
    }

    @Test
    void whenGetLongestUnseenCard_thenReturnsSameStatus_Internal() {
        HttpServerErrorException exception = HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", new HttpHeaders(), null, null);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenThrow(exception);

        Pair<HttpStatusCode, Object> result = dbQueryService.getLongestUnseenCard(1L, 2L, 3L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getLeft());
        assertEquals(result.getRight(), Optional.empty());
    }


    @Test
    void whenGetRandomPeekSessionCard_NonOkStatus() {
        HttpStatus nonOkStatus = HttpStatus.NO_CONTENT;
        ResponseEntity<String> responseEntity = new ResponseEntity<>(nonOkStatus);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        Pair<HttpStatusCode, Object> result = dbQueryService.getRandomPeekSessionCard(1L, 1L);

        assertEquals(nonOkStatus, result.getLeft());
        assertEquals(result.getRight(), Optional.empty());
    }

}
