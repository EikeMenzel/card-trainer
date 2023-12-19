package com.service.authenticationservice.payload.inc;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(@NotBlank @Size(min = 4, max = 30) String username, @Email @NotBlank @Size(min = 6, max = 64) String email, @NotBlank @Size(min = 8, max = 64) String password) {
}
