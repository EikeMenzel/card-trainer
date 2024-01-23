package com.service.cardsservice.payload.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeckNameDTO(@NotBlank @Size(min = 1, max = 128) String deckName) {
}
