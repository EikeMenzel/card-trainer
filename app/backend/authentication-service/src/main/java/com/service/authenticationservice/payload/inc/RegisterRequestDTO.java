package com.service.authenticationservice.payload.inc;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(@NotBlank String username, @NotBlank String email, @NotBlank String password) {
}
