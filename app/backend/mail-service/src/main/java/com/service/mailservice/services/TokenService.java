package com.service.mailservice.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenService {
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    public static String generateShareDeckToken(Long deckId) {
        return deckId + "-" + UUID.randomUUID();
    }
    private TokenService() {}
}
