package com.service.mailservice.service;

import com.service.mailservice.payload.out.UserTokenDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class DbQueryServiceTest {
    private static final String GENERIC_DB_API_PATH = "http://localhost:8086/api/v1/db";
    private static final String USER_TOKEN_DB_API_PATH  =  GENERIC_DB_API_PATH + "/user-token";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DbQueryService dbQueryService;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void saveUserToken_Success() {
        UserTokenDTO userTokenDTO = new UserTokenDTO("token123", Timestamp.from(Instant.now().plus(Duration.ofHours(24L))), "VERIFICATION", 1L);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> responseEntity = new ResponseEntity<>("", HttpStatus.CREATED);

        when(restTemplate.postForEntity(USER_TOKEN_DB_API_PATH + "/", new HttpEntity<>(userTokenDTO, headers), String.class))
                .thenReturn(responseEntity);

        HttpStatusCode statusCode = dbQueryService.saveUserToken(userTokenDTO);

        assertEquals(HttpStatus.CREATED, statusCode);
    }

    @Test
    void getUserEmailFromId_Success() {
        Long userId = 1L;
        String userEmail = "user@example.com";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(userEmail, HttpStatus.OK);

        when(restTemplate.getForEntity(GENERIC_DB_API_PATH + "/users/" + userId + "/email", String.class))
                .thenReturn(responseEntity);

        Optional<String> result = dbQueryService.getUserEmailFromId(userId);

        assertTrue(result.isPresent());
        assertEquals(userEmail, result.get());
    }

    @Test
    void getUserEmailFromId_UserNotFound() {
        Long userId = 1L;

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        when(restTemplate.getForEntity(GENERIC_DB_API_PATH + "/users/" + userId + "/email", String.class))
                .thenReturn(responseEntity);

        Optional<String> result = dbQueryService.getUserEmailFromId(userId);

        assertFalse(result.isPresent());
    }
}

