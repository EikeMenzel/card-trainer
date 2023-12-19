package com.service.authenticationservice.payload.inc;

import jakarta.validation.constraints.Size;

public record UpdatePasswordDTO(@Size(min = 8, max = 64) String password) {
}
