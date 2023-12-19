package com.service.authenticationservice.payload.inc;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record PasswordResetDTO(@Size(min = 36, max = 50) String token, @Email @Size(min = 6, max = 64) String email, @Size(min = 8, max = 72) String password) {
}
