package com.service.databaseservice.repository.user_token;

import com.service.databaseservice.model.user_token.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenTypeRepository extends JpaRepository<TokenType, Long> {
}
