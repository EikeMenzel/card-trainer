package com.service.authenticationservice.payload.inc;

import jakarta.validation.constraints.Size;

public record UpdatePasswordDTO(@Size(min = 8, max = 72, message = "The password needs to be at least 8 and max 72 chars long") String password) {
}
