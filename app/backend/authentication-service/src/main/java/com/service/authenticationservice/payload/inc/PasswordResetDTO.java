package com.service.authenticationservice.payload.inc;

public record PasswordResetDTO(String token, String email, String password) {
}
