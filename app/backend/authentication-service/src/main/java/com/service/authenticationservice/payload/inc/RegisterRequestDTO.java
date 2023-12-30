package com.service.authenticationservice.payload.inc;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(@NotBlank @Size(min = 4, max = 30, message = "The username needs to be between 4 and 30 chars long") String username, @Email @NotBlank @Size(min = 6, max = 64, message = "The E-mail is not valid") String email, @NotBlank @Size(min = 8, max = 72, message = "The password needs to be at least 8 and max 72 chars long") String password) {
}
