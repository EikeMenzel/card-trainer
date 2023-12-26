package com.service.authenticationservice.payload.inc;

import jakarta.validation.constraints.Size;

public record MailDTO(@Size(min = 6, max = 64, message = "E-Mail is not valid!") String email) {
}
