package com.service.authenticationservice.payload.inc;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(@NotBlank String email, @NotBlank String password) {
}
