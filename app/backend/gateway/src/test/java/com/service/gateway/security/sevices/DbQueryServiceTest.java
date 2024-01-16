package com.service.gateway.security.sevices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.gateway.model.UserDTO;
import com.service.gateway.security.services.DbQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class DbQueryServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;


    @InjectMocks
    private DbQueryService dbQueryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenUserFound_thenReturnsUser() throws JsonProcessingException {
        UserDTO testUser = new UserDTO(1L, "test", "test@gmail.com", "AAaA" );
        String mockResponse = objectMapper.writeValueAsString(testUser);

        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));


        when(objectMapper.readValue(mockResponse, UserDTO.class))
                .thenReturn(testUser);

        Optional<UserDTO> result = dbQueryService.getUserByEmail("test@gmail.com");

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
    }

    @Test
    void whenUserNotFound_thenReturnsEmpty() {
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        Optional<UserDTO> result = dbQueryService.getUserByEmail("test@gmail.com");

        assertFalse(result.isPresent());
    }

    @Test
    void whenExceptionOccurs_thenReturnsEmpty() {
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenThrow(new RestClientException("Exception occurred"));

        Optional<UserDTO> result = dbQueryService.getUserByEmail("test@gmail.com");

        assertFalse(result.isPresent());
    }


    @Test
    void whenUserIdNotFound_thenReturnsEmpty() {
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        Optional<String> result = dbQueryService.getUserEmailFromId(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void whenExceptionOccurs_IdNotFound_thenReturnsEmpty() {
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenThrow(new RestClientException("Exception occurred"));

        Optional<String> result = dbQueryService.getUserEmailFromId(1L);

        assertFalse(result.isPresent());
    }
}
