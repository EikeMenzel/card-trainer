package com.service.databaseservice.services;

import com.service.databaseservice.model.User;
import com.service.databaseservice.model.user_token.TokenType;
import com.service.databaseservice.model.user_token.UserToken;
import com.service.databaseservice.payload.out.UserTokenDTO;
import com.service.databaseservice.repository.user_token.TokenTypeRepository;
import com.service.databaseservice.repository.user_token.UserTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
//@SpringBootTest
@DataJpaTest
class UserTokenServiceTest {
    @Mock
    private UserTokenRepository userTokenRepository;

    @Mock
    private TokenTypeRepository tokenTypeRepository;

    @Mock
    private UserService userService;

    @Mock
    private Logger logger;

    @InjectMocks
    private UserTokenService userTokenService;

    @Test
    void testCreateUserToken_Success() {
        UserTokenDTO userTokenDTO = new UserTokenDTO("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), "VERIFICATION", 1L);

        Mockito.when(tokenTypeRepository.findTokenTypeByType(userTokenDTO.tokenType())).thenReturn(Optional.of(new TokenType("VERIFICATION")));
        Mockito.when(userService.getUserFromId(userTokenDTO.userId())).thenReturn(Optional.of(new User("testUser", "test@example.com", "password")));

        assertTrue(userTokenService.createUserToken(userTokenDTO));

        Mockito.verify(userTokenRepository, Mockito.times(1)).save(any(UserToken.class));
    }

    @Test
    void testCreateUserToken_InvalidTokenType() {
        UserTokenDTO userTokenDTO = new UserTokenDTO("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), "INVALID_TYPE", 1L);

        Mockito.when(tokenTypeRepository.findTokenTypeByType(userTokenDTO.tokenType())).thenReturn(Optional.empty());

        assertFalse(userTokenService.createUserToken(userTokenDTO));
    }

    @Test
    void testCreateUserToken_UserNotFound() {
        UserTokenDTO userTokenDTO = new UserTokenDTO("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), "VERIFICATION", 1L);

        Mockito.when(tokenTypeRepository.findTokenTypeByType(userTokenDTO.tokenType())).thenReturn(Optional.of(new TokenType("VERIFICATION")));
        Mockito.when(userService.getUserFromId(userTokenDTO.userId())).thenReturn(Optional.empty());

        assertFalse(userTokenService.createUserToken(userTokenDTO));
    }

    @Test
    void testCreateUserToken_False() {
        UserTokenDTO userTokenDTO = new UserTokenDTO("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), "VERIFICATION", 1L);

        Mockito.when(tokenTypeRepository.findTokenTypeByType(userTokenDTO.tokenType())).thenReturn(Optional.of(new TokenType("VERIFICATION")));
        Mockito.when(userService.getUserFromId(userTokenDTO.userId())).thenReturn(Optional.of(new User("testUser", "test@example.com", "password")));
        Mockito.when(userTokenRepository.save(any(UserToken.class))).thenThrow(new RuntimeException("Error saving user token"));

        assertFalse(userTokenService.createUserToken(userTokenDTO));
    }
}
