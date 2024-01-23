package com.service.cardsservice.payload.out;

public record EmailRequestDTO(long userId, Long deckId) {
    public static EmailRequestDTO withUserIdOnly(long userId) {
        return new EmailRequestDTO(userId, null);
    }
}
