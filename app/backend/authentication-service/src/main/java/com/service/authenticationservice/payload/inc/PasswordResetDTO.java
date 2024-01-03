package com.service.authenticationservice.payload.inc;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record PasswordResetDTO(String token, @Email String email, String password) {
}
