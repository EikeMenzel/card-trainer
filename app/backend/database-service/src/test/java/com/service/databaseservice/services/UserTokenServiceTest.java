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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserTokenServiceTest {
    @Mock
    private UserTokenRepository userTokenRepository;
    @Mock
    private TokenTypeRepository tokenTypeRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserTokenService userTokenService;

    @Test
    void testCreateUserToken_Success() {
        UserTokenDTO userTokenDTO = new UserTokenDTO("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), "VERIFICATION", 1L);

        when(tokenTypeRepository.findTokenTypeByType(userTokenDTO.tokenType())).thenReturn(Optional.of(new TokenType("VERIFICATION")));
        when(userService.getUserFromId(userTokenDTO.userId())).thenReturn(Optional.of(new User("testUser", "test@example.com", "password", false)));

        assertTrue(userTokenService.createUserToken(userTokenDTO));

        verify(userTokenRepository, times(1)).save(any(UserToken.class));
    }

    @Test
    void testCreateUserToken_InvalidTokenType() {
        UserTokenDTO userTokenDTO = new UserTokenDTO("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), "INVALID_TYPE", 1L);

        when(tokenTypeRepository.findTokenTypeByType(userTokenDTO.tokenType())).thenReturn(Optional.empty());

        assertFalse(userTokenService.createUserToken(userTokenDTO));
    }

    @Test
    void testCreateUserToken_UserNotFound() {
        UserTokenDTO userTokenDTO = new UserTokenDTO("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), "VERIFICATION", 1L);

        when(tokenTypeRepository.findTokenTypeByType(userTokenDTO.tokenType())).thenReturn(Optional.of(new TokenType("VERIFICATION")));
        when(userService.getUserFromId(userTokenDTO.userId())).thenReturn(Optional.empty());

        assertFalse(userTokenService.createUserToken(userTokenDTO));
    }

    @Test
    void testCreateUserToken_False() {
        UserTokenDTO userTokenDTO = new UserTokenDTO("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), "VERIFICATION", 1L);

        when(tokenTypeRepository.findTokenTypeByType(userTokenDTO.tokenType())).thenReturn(Optional.of(new TokenType("VERIFICATION")));
        when(userService.getUserFromId(userTokenDTO.userId())).thenReturn(Optional.of(new User("testUser", "test@example.com", "password", false)));
        when(userTokenRepository.save(any(UserToken.class))).thenThrow(new RuntimeException("Error saving user token"));

        assertFalse(userTokenService.createUserToken(userTokenDTO));
    }

    @Test
    void testIsUserTokenValid() {
        UserToken userToken_Valid = new UserToken("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), new TokenType("VERIFICATION"), new User("testUser", "test@example.com", "password", false));
        UserToken userToken_Invalid_Timestamp = new UserToken("expired", Timestamp.from(Instant.now().minusSeconds(3600)), new TokenType("VERIFICATION"), new User("testUser", "test@example.com", "password", false));

        when(userTokenRepository.getUserTokenByTokenValue("tokenValue")).thenReturn(Optional.of(userToken_Valid));
        when(userTokenRepository.getUserTokenByTokenValue("expired")).thenReturn(Optional.of(userToken_Invalid_Timestamp));

        assertTrue(userTokenService.isUserTokenValid("tokenValue"));
        assertFalse(userTokenService.isUserTokenValid("expired"));

        verify(userTokenRepository, times(2)).getUserTokenByTokenValue(anyString());
    }

    @Test
    void testIsTokenVerificationToken() {
        UserToken userToken_Verification = new UserToken("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), new TokenType("VERIFICATION"), new User("testUser", "test@example.com", "password", false));
        UserToken userToken_Random = new UserToken("random", Timestamp.from(Instant.now().plusSeconds(3600)), new TokenType("RANDOM"), new User("testUser", "test@example.com", "password", false));

        when(userTokenRepository.getUserTokenByTokenValue("tokenValue")).thenReturn(Optional.of(userToken_Verification));
        when(userTokenRepository.getUserTokenByTokenValue("random")).thenReturn(Optional.of(userToken_Random));

        assertTrue(userTokenService.areTokenTypesIdentical("tokenValue", "VERIFICATION"));
        assertFalse(userTokenService.areTokenTypesIdentical("random", "VERIFICATION"));

        verify(userTokenRepository, times(2)).getUserTokenByTokenValue(anyString());
    }

    @Test
    void testSetUserEmailAsVerified() {
        UserToken verificationToken = new UserToken("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), new TokenType("VERIFICATION"), new User("testUser", "test@example.com", "password", false));

        when(userTokenRepository.getUserTokenByTokenValue("tokenValue")).thenReturn(Optional.of(verificationToken));

        assertTrue(userTokenService.setUserEmailAsVerified("tokenValue"));

        verify(userTokenRepository, times(2)).getUserTokenByTokenValue(anyString());
        verify(userTokenRepository, times(1)).save(any(UserToken.class));
    }

    @Test
    void testSetUserEmailAsVerified_InvalidToken() {
        when(userTokenRepository.getUserTokenByTokenValue("invalidToken")).thenReturn(Optional.empty());

        assertFalse(userTokenService.setUserEmailAsVerified("invalidToken"));

        verify(userTokenRepository, times(1)).getUserTokenByTokenValue(anyString());
        verify(userTokenRepository, never()).save(any(UserToken.class));
    }

    @Test
    void testSetUserEmailAsVerified_NonVerificationToken() {
        UserToken randomToken = new UserToken("random", Timestamp.from(Instant.now().plusSeconds(3600)), new TokenType("RANDOM"), new User("testUser", "test@example.com", "password", false));

        when(userTokenRepository.getUserTokenByTokenValue("random")).thenReturn(Optional.of(randomToken));

        assertFalse(userTokenService.setUserEmailAsVerified("random"));

        verify(userTokenRepository, times(2)).getUserTokenByTokenValue(anyString());
        verify(userTokenRepository, never()).save(any(UserToken.class));
    }

    @Test
    void belongsTokenToUser_WhenTokenBelongsToUser() {
        User user = new User(1L, "username", "email@example.com", "password",false, false, 10, "en");
        UserToken randomToken = new UserToken("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), new TokenType("RANDOM"),
                user);

        when(userTokenRepository.getUserTokenByTokenValue("tokenValue")).thenReturn(Optional.of(randomToken));

        assertTrue(userTokenService.belongsTokenToUser(1L, "tokenValue"));
    }

    @Test
    void belongsTokenToUser_WhenTokenDoesNotBelongToUser() {
        User user1 = new User(1L, "username", "email@example.com", "password",false, false, 10, "en");
        User user2 = new User(2L, "username", "email@example.com", "password",false, false, 10, "en");

        UserToken randomToken = new UserToken("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), new TokenType("RANDOM"),
                user1);


        when(userTokenRepository.getUserTokenByTokenValue("tokenValue")).thenReturn(Optional.of(randomToken));

        assertFalse(userTokenService.belongsTokenToUser(user2.getId(), "tokenValue"));
    }

    @Test
    void isUserWithTokenVerified_WhenUserIsVerified() {
        User user = new User(1L, "username", "email@example.com", "password",true, false, 10, "en");
        UserToken randomToken = new UserToken("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), new TokenType("RANDOM"),
                user);

        when(userTokenRepository.getUserTokenByTokenValue(randomToken.getTokenValue())).thenReturn(Optional.of(randomToken));

        assertTrue(userTokenService.isUserWithTokenVerified(randomToken.getTokenValue()));
    }

    @Test
    void isUserWithTokenVerified_WhenUserIsNotVerified() {
        User user = new User(1L, "username", "email@example.com", "password",false, false, 10, "en");
        UserToken randomToken = new UserToken("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), new TokenType("RANDOM"),
                user);

        when(userTokenRepository.getUserTokenByTokenValue(randomToken.getTokenValue())).thenReturn(Optional.of(randomToken));

        assertFalse(userTokenService.isUserWithTokenVerified(randomToken.getTokenValue()));
    }

    @Test
    void areTokenTypesIdentical_WhenTypesMatch() {
        TokenType type = new TokenType("tokenType");

        User user = new User(1L, "username", "email@example.com", "password",false, false, 10, "en");
        UserToken userToken = new UserToken("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), type, user);

        when(userTokenRepository.getUserTokenByTokenValue(userToken.getTokenValue())).thenReturn(Optional.of(userToken));

        assertTrue(userTokenService.areTokenTypesIdentical("tokenValue", "tokenType"));
    }

    @Test
    void areTokenTypesIdentical_WhenTypesDoNotMatch() {
        TokenType type = new TokenType("tokenType");

        User user = new User(1L, "username", "email@example.com", "password",false, false, 10, "en");
        UserToken userToken = new UserToken("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), type, user);

        when(userTokenRepository.getUserTokenByTokenValue(userToken.getTokenValue())).thenReturn(Optional.of(userToken));

        assertFalse(userTokenService.areTokenTypesIdentical("tokenValue", "FalseType"));
    }

    @Test
    void deleteToken_WhenTokenBelongsToUser() {
        TokenType type = new TokenType("tokenType");

        User user = new User(1L, "username", "email@example.com", "password",false, false, 10, "en");
        UserToken userToken = new UserToken("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), type, user);

        when(userTokenRepository.getUserTokenByTokenValue(any())).thenReturn(Optional.of(userToken));

        assertTrue(userTokenService.deleteToken(1L, "tokenValue"));
        verify(userTokenRepository).delete(userToken);
    }

    @Test
    void deleteToken_WhenTokenDoesNotBelongToUser() {
        TokenType type = new TokenType("tokenType");

        User user = new User(1L, "username", "email@example.com", "password",false, false, 10, "en");
        UserToken userToken = new UserToken("tokenValue", Timestamp.from(Instant.now().plusSeconds(3600)), type, user);

        Long userId = 2L; // Different from user's ID

        when(userTokenRepository.getUserTokenByTokenValue(any())).thenReturn(Optional.of(userToken));

        assertFalse(userTokenService.deleteToken(userId, "tokenValue"));
        verify(userTokenRepository, never()).delete(any(UserToken.class));
    }

    @Test
    void getDeckIdByUserToken_WhenTokenIsValid() {
        TokenType type = new TokenType("SHARE_DECK");

        User user = new User(1L, "username", "email@example.com", "password",false, false, 10, "en");
        UserToken userToken = new UserToken("123-Deck", Timestamp.from(Instant.now().plusSeconds(3600)), type, user);

        when(userTokenRepository.getUserTokenByTokenValue(any())).thenReturn(Optional.of(userToken));

        assertEquals(Optional.of(123L), userTokenService.getDeckIdByUserToken(userToken.getTokenValue()));
    }

    @Test
    void getDeckIdByUserToken_WhenTokenIsInvalid() {
        TokenType type = new TokenType("SHARE_DECK");

        User user = new User(1L, "username", "email@example.com", "password",false, false, 10, "en");
        UserToken userToken = new UserToken("-1-Deck", Timestamp.from(Instant.now().plusSeconds(3600)), type, user);


        when(userTokenRepository.getUserTokenByTokenValue(any())).thenReturn(Optional.of(userToken));

        assertEquals(Optional.empty(), userTokenService.getDeckIdByUserToken(userToken.getTokenValue()));
    }

    @Test
    void getUserByUserToken_WhenTokenIsValid() {
        TokenType type = new TokenType("SHARE_DECK");

        User user = new User(1L, "username", "email@example.com", "password",false, false, 10, "en");
        UserToken userToken = new UserToken("1-Deck", Timestamp.from(Instant.now().plusSeconds(3600)), type, user);

        when(userTokenRepository.getUserTokenByTokenValue(any())).thenReturn(Optional.of(userToken));

        assertEquals(Optional.of(user), userTokenService.getUserByUserToken("1-Deck"));
    }

    @Test
    void getUserByUserToken_WhenTokenIsInvalid() {
        TokenType type = new TokenType("OTHER_TYPE");

        User user = new User(1L, "username", "email@example.com", "password",false, false, 10, "en");
        UserToken userToken = new UserToken("tokenValue", Timestamp.from(Instant.now().minusSeconds(3600)), type, user);

        when(userTokenRepository.getUserTokenByTokenValue(any())).thenReturn(Optional.of(userToken));

        assertEquals(Optional.empty(), userTokenService.getUserByUserToken("tokenValue"));
    }
}
