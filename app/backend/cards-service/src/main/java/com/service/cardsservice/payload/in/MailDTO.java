package com.service.cardsservice.payload.in;

import jakarta.validation.constraints.Size;

public record MailDTO(@Size(min = 6, max = 64) String email) {
}
