package com.service.mailservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.mailservice.payload.inc.UserAccountInformationDTO;
import com.service.mailservice.payload.inc.UserDailyReminderDTO;
import com.service.mailservice.payload.out.UserTokenDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
class DbQueryServiceTest {

    @Value("${db.api.path}/users")
    private String USER_DB_API_PATH;

    @Value("${db.api.path}/user-token")
    private String USER_TOKEN_DB_API_PATH;

    @MockBean
    private ObjectMapper objectMapper;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private DbQueryService dbQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    void testSaveUserToken() {
        UserTokenDTO userTokenDTO = new UserTokenDTO("token123", Timestamp.from(Instant.now().plus(Duration.ofHours(24L))), "VERIFICATION", 1L);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        HttpStatusCode result = dbQueryService.saveUserToken(userTokenDTO);

        assertEquals(HttpStatus.OK, result);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void getUserEmailFromId_Success() {
        Long userId = 1L;
        String userEmail = "user@example.com";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(userEmail, HttpStatus.OK);

        when(restTemplate.getForEntity(USER_DB_API_PATH + "/" + userId + "/email", String.class))
                .thenReturn(responseEntity);

        Optional<String> result = dbQueryService.getUserEmailFromId(userId);

        assertTrue(result.isPresent());
        assertEquals(userEmail, result.get());
    }

    @Test
    void testGetUserEmailFromId() {
        Long userId = 1L;
        String userEmail = "test@example.com";
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(new ResponseEntity<>(userEmail, HttpStatus.OK));

        Optional<String> result = dbQueryService.getUserEmailFromId(userId);

        assertEquals(userEmail, result.orElse(null));
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(String.class));
    }

    @Test
    void getUserEmailFromId_UserNotFound() {
        Long userId = 1L;

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        when(restTemplate.getForEntity(USER_DB_API_PATH + "/" + userId + "/email", String.class))
                .thenReturn(responseEntity);

        Optional<String> result = dbQueryService.getUserEmailFromId(userId);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllEmailsForDailyLearn() throws JsonProcessingException {
        List<UserDailyReminderDTO> userReminderList = Arrays.asList(
                new UserDailyReminderDTO("user1", "user1@example.com"),
                new UserDailyReminderDTO("user2", "user2@example.com")
        );
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(new ResponseEntity<>(userReminderList.toString(), HttpStatus.OK));
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(userReminderList);

        Optional<List<UserDailyReminderDTO>> result = dbQueryService.getAllEmailsForDailyLearn();

        assertEquals(userReminderList, result.orElse(null));
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(String.class));
    }

    @Test
    void testGetDeckNameByDeckId() {
        Long deckId = 1L;
        String deckName = "Deck 1";
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(new ResponseEntity<>(deckName, HttpStatus.OK));

        Optional<String> result = dbQueryService.getDeckNameByDeckId(deckId);

        assertEquals(deckName, result.orElse(null));
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(String.class));
    }

    @Test
    void testGetAccountInformation() throws JsonProcessingException {
        Long userId = 1L;
        UserAccountInformationDTO userAccountInfo = new UserAccountInformationDTO("username", "test@example.com", 10, false, "en");
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(new ResponseEntity<>(userAccountInfo.toString(), HttpStatus.OK));
        when(objectMapper.readValue(anyString(), eq(UserAccountInformationDTO.class))).thenReturn(userAccountInfo);

        Optional<UserAccountInformationDTO> result = dbQueryService.getAccountInformation(userId);

        assertEquals(userAccountInfo, result.orElse(null));
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(String.class));
    }

    @Test
    void testGetUserEmailFromId_ReturnsEmpty() {
        Long userId = 1L;
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(new ResponseEntity<>("", HttpStatus.NOT_FOUND));

        Optional<String> result = dbQueryService.getUserEmailFromId(userId);

        assertEquals(Optional.empty(), result);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(String.class));
    }

    @Test
    void testGetAllEmailsForDailyLearn_ReturnsEmpty() {
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(new ResponseEntity<>("", HttpStatus.NOT_FOUND));

        Optional<List<UserDailyReminderDTO>> result = dbQueryService.getAllEmailsForDailyLearn();

        assertEquals(Optional.empty(), result);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(String.class));
    }

    @Test
    void testGetDeckNameByDeckId_ReturnsEmpty() {
        Long deckId = 1L;
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(new ResponseEntity<>("", HttpStatus.NOT_FOUND));

        Optional<String> result = dbQueryService.getDeckNameByDeckId(deckId);

        assertEquals(Optional.empty(), result);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(String.class));
    }

    @Test
    void testGetAccountInformation_ReturnsEmpty() {
        Long userId = 1L;
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(new ResponseEntity<>("", HttpStatus.NOT_FOUND));

        Optional<UserAccountInformationDTO> result = dbQueryService.getAccountInformation(userId);

        assertEquals(Optional.empty(), result);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(String.class));
    }
}

