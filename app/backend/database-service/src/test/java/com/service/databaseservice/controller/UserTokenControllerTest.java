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
import static org.mockito.Mockito.when;

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
}
