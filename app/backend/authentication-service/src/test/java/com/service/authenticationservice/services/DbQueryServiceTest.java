package com.service.authenticationservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.authenticationservice.payload.inc.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
class DbQueryServiceTest {
    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private ObjectMapper objectMapper;
    @Autowired
    private DbQueryService dbQueryService;

    @Value("${db.api.path}/users")
    private String USER_DB_API_PATH;

    @Value("${db.api.path}/users/emails")
    private String USER_EMAIL_DB_API_PATH;
    @BeforeEach
    void setUp() throws JsonProcessingException {
        Mockito.when(objectMapper.readValue(Mockito.anyString(), Mockito.eq(UserDTO.class)))
                .thenReturn(new UserDTO(1L, "TestUser", "password1233!", "test@example.com", false));
    }

    @Test
    void testGetUserByEmail_Success() {
        String email = "test@example.com";
        String expectedUrl = USER_EMAIL_DB_API_PATH + "/" + email;
        ResponseEntity<String> responseEntity = new ResponseEntity<>("{ \"userId\": 123, \"email\": \"test@example.com\" }", HttpStatus.OK);

        Mockito.when(restTemplate.getForEntity(expectedUrl, String.class))
                .thenReturn(responseEntity);

        Optional<UserDTO> result = dbQueryService.getUserByEmail(email);

        assertTrue(result.isPresent());
    }

    @Test
    void testGetUserByEmail_NotFound() {
        String email = "nonexistent@example.com";
        String expectedUrl = USER_EMAIL_DB_API_PATH + "/" + email;
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Mockito.when(restTemplate.getForEntity(expectedUrl, String.class))
                .thenReturn(responseEntity);

        Optional<UserDTO> result = dbQueryService.getUserByEmail(email);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetUserIdByEmail_Success() {
        String email = "test@example.com";
        String expectedUrl = USER_EMAIL_DB_API_PATH + "/" + email + "/id";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("123", HttpStatus.OK);

        Mockito.when(restTemplate.getForEntity(expectedUrl, String.class))
                .thenReturn(responseEntity);

        Optional<Long> result = dbQueryService.getUserIdByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(123L, result.get());
    }

    @Test
    void testGetUserIdByEmail_NotFound() {
        String email = "nonexistent@example.com";
        String expectedUrl = USER_EMAIL_DB_API_PATH + "/" + email + "/id";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Mockito.when(restTemplate.getForEntity(expectedUrl, String.class))
                .thenReturn(responseEntity);

        Optional<Long> result = dbQueryService.getUserIdByEmail(email);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDoesUserWithEmailExist_True() {
        String email = "test@example.com";
        String expectedUrl = USER_EMAIL_DB_API_PATH + "/" + email + "/exists";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("true", HttpStatus.OK);

        Mockito.when(restTemplate.getForEntity(expectedUrl, String.class))
                .thenReturn(responseEntity);

        Optional<Boolean> result = dbQueryService.doesUserWithEmailExist(email);

        assertTrue(result.isPresent());
        assertTrue(result.get());
    }


    @Test
    void testDoesUserWithEmailExist_False() {
        String email = "nonexistent@example.com";
        String expectedUrl = USER_EMAIL_DB_API_PATH + "/" + email + "/exists";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("", HttpStatus.NOT_FOUND);

        Mockito.when(restTemplate.getForEntity(expectedUrl, String.class))
                .thenReturn(responseEntity);

        Optional<Boolean> result = dbQueryService.doesUserWithEmailExist(email);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetUserVerificationState_True() {
        long userId = 1L;
        String expectedUrl = USER_DB_API_PATH + "/" + userId + "/verified";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("true", HttpStatus.OK);

        Mockito.when(restTemplate.getForEntity(expectedUrl, String.class))
                .thenReturn(responseEntity);

        Optional<Boolean> result = dbQueryService.getVerificationStateUser(userId);

        assertTrue(result.isPresent());
        assertTrue(result.get());
    }


    @Test
    void testGetUserVerificationState_False() {
        long userId = 1L;
        String expectedUrl = USER_DB_API_PATH + "/" + userId + "/verified";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("", HttpStatus.NOT_FOUND);

        Mockito.when(restTemplate.getForEntity(expectedUrl, String.class))
                .thenReturn(responseEntity);

        Optional<Boolean> result = dbQueryService.getVerificationStateUser(userId);

        assertTrue(result.isEmpty());
    }


    @Test
    void testSaveUser_Success() {
        UserDTO userDTO = new UserDTO(1L, "TestUser", "password1233!", "test@example.com", false);
        String expectedUrl = USER_DB_API_PATH + "/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);

        Mockito.when(restTemplate.postForEntity(expectedUrl, new HttpEntity<>(userDTO, headers), String.class))
                .thenReturn(responseEntity);

        HttpStatusCode result = dbQueryService.saveUser(userDTO);

        assertEquals(HttpStatus.CREATED, result);
    }

    @Test
    void testSaveUser_Failure() {
        UserDTO userDTO = new UserDTO(1L, "TestUser", "password1233!", "test@example.com", false);
        String expectedUrl = USER_DB_API_PATH + "/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        Mockito.when(restTemplate.postForEntity(expectedUrl, new HttpEntity<>(userDTO, headers), String.class))
                .thenReturn(responseEntity);

        HttpStatusCode result = dbQueryService.saveUser(userDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result);
    }

    @Test
    void getUserEmailFromId_Success() {
        Long userId = 1L;
        String userEmail = "user@example.com";
        String expectedUrl = USER_DB_API_PATH + "/";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(userEmail, HttpStatus.OK);

        when(restTemplate.getForEntity(expectedUrl + userId + "/email", String.class))
                .thenReturn(responseEntity);

        Optional<String> result = dbQueryService.getUserEmailFromId(userId);

        assertTrue(result.isPresent());
        assertEquals(userEmail, result.get());
    }

    @Test
    void getUserEmailFromId_UserNotFound() {
        Long userId = 1L;
        String expectedUrl = USER_DB_API_PATH + "/";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        when(restTemplate.getForEntity(expectedUrl + userId + "/email", String.class))
                .thenReturn(responseEntity);

        Optional<String> result = dbQueryService.getUserEmailFromId(userId);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateUserWithToken_Success() {
        ResponseEntity<Void> successResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(successResponse);

        assertEquals(HttpStatus.NO_CONTENT, dbQueryService.setVerificationStateToTrue("validToken"));
    }

    @Test
    void testUpdateUserWithToken_Conflict() {
        ResponseEntity<Void> conflictResponse = new ResponseEntity<>(HttpStatus.CONFLICT);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.CONFLICT));

        assertEquals(HttpStatus.CONFLICT, dbQueryService.setVerificationStateToTrue("conflictToken"));
    }

    @Test
    void testUpdateUserWithToken_BadRequest() {
        ResponseEntity<Void> badRequestResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        assertEquals(HttpStatus.BAD_REQUEST, dbQueryService.setVerificationStateToTrue("badRequestToken"));
    }

    @Test
    void testUpdateUserWithToken_InternalServerError() {
        ResponseEntity<Void> internalServerErrorResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, dbQueryService.setVerificationStateToTrue("serverErrorToken"));
    }

}
