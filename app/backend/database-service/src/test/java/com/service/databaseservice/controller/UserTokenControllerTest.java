package com.service.databaseservice.controller;

import com.service.databaseservice.payload.out.UserTokenDTO;
import com.service.databaseservice.services.UserTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserTokenControllerTest {

    @Mock
    private UserTokenService userTokenService;

    @InjectMocks
    private UserTokenController userTokenController;

    @Test
    void testCreateUserToken_Success() {
        UserTokenDTO userTokenDTO = new UserTokenDTO("tokenValue", Timestamp.from(Instant.now().plus(Duration.ofHours(24L))), "VERIFICATION", 1L);

        when(userTokenService.createUserToken(userTokenDTO)).thenReturn(true);

        ResponseEntity<?> response = userTokenController.createUserToken(userTokenDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testCreateUserToken_InvalidTokenType() {
        UserTokenDTO userTokenDTO = new UserTokenDTO("tokenValue", Timestamp.from(Instant.now().plus(Duration.ofHours(24L))), "INVALID_TYPE", 1L);

        when(userTokenService.createUserToken(userTokenDTO)).thenReturn(false);

        ResponseEntity<?> response = userTokenController.createUserToken(userTokenDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testCreateUserToken_UserNotFound() {
        UserTokenDTO userTokenDTO = new UserTokenDTO("tokenValue", Timestamp.from(Instant.now().plus(Duration.ofHours(24L))), "VERIFICATION", 1L);

        when(userTokenService.createUserToken(userTokenDTO)).thenReturn(false);

        ResponseEntity<?> response = userTokenController.createUserToken(userTokenDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    //Put Route
    @Test
    void testUpdateUserWithToken_ValidVerificationToken() {
        when(userTokenService.isUserTokenValid("tokenValue")).thenReturn(true);
        when(userTokenService.areTokenTypesIdentical("tokenValue", "VERIFICATION")).thenReturn(true);
        when(userTokenService.isUserWithTokenVerified("tokenValue")).thenReturn(false);
        when(userTokenService.setUserEmailAsVerified("tokenValue")).thenReturn(true);

        ResponseEntity<?> response = userTokenController.updateUserWithToken("tokenValue");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(userTokenService, times(1)).isUserTokenValid(anyString());
        verify(userTokenService, times(1)).areTokenTypesIdentical(anyString(), anyString());
        verify(userTokenService, times(1)).isUserWithTokenVerified(anyString());
        verify(userTokenService, times(1)).setUserEmailAsVerified(anyString());
    }

    @Test
    void testUpdateUserWithToken_InvalidToken() {
        when(userTokenService.isUserTokenValid("invalidToken")).thenReturn(false);

        ResponseEntity<?> response = userTokenController.updateUserWithToken("invalidToken");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        verify(userTokenService, times(1)).isUserTokenValid(anyString());
        verify(userTokenService, never()).areTokenTypesIdentical(anyString(), anyString());
        verify(userTokenService, never()).isUserWithTokenVerified(anyString());
        verify(userTokenService, never()).setUserEmailAsVerified(anyString());
    }

    @Test
    void testUpdateUserWithToken_ConflictVerificationToken() {
        when(userTokenService.isUserTokenValid("conflictToken")).thenReturn(true);
        when(userTokenService.areTokenTypesIdentical("conflictToken", "VERIFICATION")).thenReturn(true);
        when(userTokenService.isUserWithTokenVerified("conflictToken")).thenReturn(true);

        ResponseEntity<?> response = userTokenController.updateUserWithToken("conflictToken");

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        verify(userTokenService, times(1)).isUserTokenValid(anyString());
        verify(userTokenService, times(1)).areTokenTypesIdentical(anyString(), anyString());
        verify(userTokenService, times(1)).isUserWithTokenVerified(anyString());
        verify(userTokenService, never()).setUserEmailAsVerified(anyString());
    }

    @Test
    void testUpdateUserWithToken_UnprocessableEntity() {
        when(userTokenService.isUserTokenValid("nonVerificationToken")).thenReturn(true);
        when(userTokenService.areTokenTypesIdentical("nonVerificationToken", "VERIFICATION")).thenReturn(false);

        ResponseEntity<?> response = userTokenController.updateUserWithToken("nonVerificationToken");

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());

        verify(userTokenService, times(1)).isUserTokenValid(anyString());
        verify(userTokenService, times(1)).areTokenTypesIdentical(anyString(), anyString());
        verify(userTokenService, never()).isUserWithTokenVerified(anyString());
        verify(userTokenService, never()).setUserEmailAsVerified(anyString());
    }
}
