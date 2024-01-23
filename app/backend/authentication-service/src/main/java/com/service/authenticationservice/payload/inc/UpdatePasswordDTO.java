package com.service.authenticationservice.payload.inc;

import org.springframework.lang.NonNull;

public record UpdatePasswordDTO(@NonNull String password) {
}
