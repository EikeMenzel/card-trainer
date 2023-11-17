package com.service.databaseservice.repository.user_token;

import com.service.databaseservice.model.user_token.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenTypeRepository extends JpaRepository<TokenType, Long> {
    Optional<TokenType> findTokenTypeByType(String type);
}
