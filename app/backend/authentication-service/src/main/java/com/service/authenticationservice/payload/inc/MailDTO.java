package com.service.authenticationservice.payload.inc;

import jakarta.validation.constraints.Size;

public record MailDTO(@Size(min = 6, max = 64) String email) {
}
