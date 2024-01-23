package com.service.userservice.services;

import java.util.regex.Pattern;

public class EmailValidator {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9][a-zA-Z0-9.-]*\\.[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})?$";
    public static boolean validate(String email) {
        if(email == null)
            return false;
        return (Pattern.compile(EMAIL_REGEX).matcher(email).matches()) && (email.length() <= 64) && (email.length() > 0);
    }
    private EmailValidator() {}
}
