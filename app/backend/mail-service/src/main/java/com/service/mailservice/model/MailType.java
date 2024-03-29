package com.service.mailservice.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum MailType {
    VERIFICATION,
    DAILY_REMINDER,
    PASSWORD_RESET,
    SHARE_DECK;

    private static final Map<String, MailType> lookup = new HashMap<>();

    static {
        for (MailType type : MailType.values()) {
            lookup.put(type.name().toLowerCase(), type);
        }
    }

    public static Optional<MailType> fromString(String value) {
        var mailType = lookup.get(value.toLowerCase());
        return mailType == null ?
                Optional.empty() :
                Optional.of(mailType);
    }
}
