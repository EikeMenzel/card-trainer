package com.service.mailservice.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TokenServiceTest {
    @Test
    void testGenerateToken() {
        // Generate two tokens and ensure they are not equal
        String token1 = TokenService.generateToken();
        String token2 = TokenService.generateToken();

        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2);
    }
}
