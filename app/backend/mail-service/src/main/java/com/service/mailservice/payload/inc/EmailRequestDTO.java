package com.service.mailservice.payload.inc;

public record EmailRequestDTO(long userId, Long deckId) {
    public static EmailRequestDTO withUserIdOnly(long userId) {
        return new EmailRequestDTO(userId, null);
    }
}
