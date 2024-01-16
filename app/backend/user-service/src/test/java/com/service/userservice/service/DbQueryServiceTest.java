package com.service.userservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.userservice.payload.inc.UserAccountInformationDTO;
import com.service.userservice.services.DbQueryService;
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
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
public class DbQueryServiceTest {

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private ObjectMapper objectMapper;

    @Autowired
    private DbQueryService dbQueryService;

    @Test
    void whenGetAccountInformation_ReturnsAccountInfo() throws Exception {
        String mockResponse = "{\"email\":\"test@example.com\"}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(any(String.class), eq(String.class))).thenReturn(responseEntity);
        when(objectMapper.readValue(any(String.class), eq(UserAccountInformationDTO.class)))
                .thenReturn(new UserAccountInformationDTO("Some username", "test@example.com", 10, false, "en", 3));

        Optional<UserAccountInformationDTO> result = dbQueryService.getAccountInformation(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void whenGetAccountInformation_UserNotFound_ThenReturnEmpty() {
        when(restTemplate.getForEntity(any(String.class), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Optional<UserAccountInformationDTO> result = dbQueryService.getAccountInformation(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void whenGetAchievementIds_ReturnsListOfIds() throws Exception {
        String mockResponse = "[1, 2, 3]";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(any(String.class), eq(String.class))).thenReturn(responseEntity);
        when(objectMapper.readValue(any(String.class), any(TypeReference.class)))
                .thenReturn(List.of(1L, 2L, 3L));

        List<Long> result = dbQueryService.getAchievementIds(1L);

        assertEquals(3, result.size());
    }

    @Test
    void whenGetAchievementIds_Fails_ThenReturnEmptyList() {
        when(restTemplate.getForEntity(any(String.class), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        List<Long> result = dbQueryService.getAchievementIds(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void whenUpdateAccountInformation_Success_ReturnsUpdatedInfo() throws Exception {
        UserAccountInformationDTO updatedInfo = new UserAccountInformationDTO("Some username", "test@example.com", 10, false, "en", 3);
        String mockResponse = objectMapper.writeValueAsString(new UserAccountInformationDTO("Some username", "test@example.com", 10, false, "en", 3));
        ResponseEntity<String> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);
        when(objectMapper.readValue(mockResponse, UserAccountInformationDTO.class)).thenReturn(updatedInfo);

        Pair<Optional<UserAccountInformationDTO>, HttpStatusCode> result = dbQueryService.updateAccountInformation(1L, updatedInfo);

        assertTrue(result.getLeft().isPresent());
        assertEquals("test@example.com", result.getLeft().get().getEmail());
        assertEquals(HttpStatus.OK, result.getRight());
    }

    @Test
    void whenUpdateAccountInformation_Fails_ReturnsEmpty() {
        UserAccountInformationDTO updatedInfo = new UserAccountInformationDTO("Some username", "test@example.com", 10, false, "en", 3);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Pair<Optional<UserAccountInformationDTO>, HttpStatusCode> result = dbQueryService.updateAccountInformation(1L, updatedInfo);

        assertFalse(result.getLeft().isPresent());
        assertEquals(HttpStatus.NOT_FOUND, result.getRight());
    }
}
