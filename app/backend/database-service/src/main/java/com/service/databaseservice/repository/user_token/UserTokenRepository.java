package com.service.databaseservice.repository.user_token;

import com.service.databaseservice.model.user_token.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> getUserTokenByTokenValue(String token);
}
