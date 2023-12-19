package com.service.authenticationservice.payload.inc;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO(@Email @NotBlank @Size(min = 6, max = 64) String email, @NotBlank @Size(min = 8, max = 72) String password) {
}
