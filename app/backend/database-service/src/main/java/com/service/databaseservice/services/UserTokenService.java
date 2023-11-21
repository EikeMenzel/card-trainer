package com.service.databaseservice.services;

import com.service.databaseservice.model.User;
import com.service.databaseservice.model.user_token.TokenType;
import com.service.databaseservice.model.user_token.UserToken;
import com.service.databaseservice.payload.out.UserTokenDTO;
import com.service.databaseservice.repository.user_token.TokenTypeRepository;
import com.service.databaseservice.repository.user_token.UserTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class UserTokenService {
    private final Logger logger =  LoggerFactory.getLogger(UserTokenService.class);
    private final UserTokenRepository userTokenRepository;
    private final TokenTypeRepository tokenTypeRepository;
    private final UserService userService;

    public UserTokenService(UserTokenRepository userTokenRepository,  TokenTypeRepository tokenTypeRepository, UserService userService) {
        this.userTokenRepository = userTokenRepository;
        this.tokenTypeRepository = tokenTypeRepository;
        this.userService = userService;
    }

    public boolean createUserToken(UserTokenDTO userTokenDTO) {
        try {
            Optional<TokenType> tokenType = tokenTypeRepository.findTokenTypeByType(String.valueOf(userTokenDTO.tokenType()));

            if(tokenType.isEmpty()) {
                logger.error(String.format("Token-type was not valid: %s", userTokenDTO.tokenType()));
                return false;
            }

            Optional<User> userOptional = userService.getUserFromId(userTokenDTO.userId());
            if(userOptional.isEmpty()) {
                logger.error(String.format("No user with this id found: %d", userTokenDTO.userId()));
                return false;
            }

            userTokenRepository.save(new UserToken(userTokenDTO.tokenValue(), userTokenDTO.expiryTimestamp(), tokenType.get(), userOptional.get()));
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public boolean isUserTokenValid(String token) {
        Optional<UserToken> userToken = userTokenRepository.getUserTokenByTokenValue(token);
        return userToken.map(value -> value.getExpiryTimestamp().after(Timestamp.from(Instant.now()))).orElse(false);
    }

    public boolean isUserWithTokenVerified(String token) {
        Optional<UserToken> userToken = userTokenRepository.getUserTokenByTokenValue(token);
        return userToken.isPresent() && userToken.get().getUser().getVerified();
    }

    public boolean isTokenVerificationToken(String token) {
        Optional<UserToken> userToken = userTokenRepository.getUserTokenByTokenValue(token);
        return userToken.isPresent() && userToken.get().getTokenType().getType().equals("VERIFICATION");
    }

    public boolean setUserEmailAsVerified(String token) {
        try {
            Optional<UserToken> userToken = userTokenRepository.getUserTokenByTokenValue(token);
            if(userToken.isEmpty() || !isTokenVerificationToken(token))
                return false;

            userToken.get().getUser().setVerified(true);
            userTokenRepository.save(userToken.get());
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
