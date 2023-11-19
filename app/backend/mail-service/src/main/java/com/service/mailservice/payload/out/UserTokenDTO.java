package com.service.mailservice.payload.out;

import java.sql.Timestamp;

public record UserTokenDTO(String tokenValue, Timestamp expiryTimestamp, String tokenType, long userId) {
}
