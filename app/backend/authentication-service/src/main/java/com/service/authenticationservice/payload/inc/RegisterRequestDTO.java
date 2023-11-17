package com.service.authenticationservice.payload.inc;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(@NotBlank String username, @Email @NotBlank String email, @NotBlank String password) {
}
