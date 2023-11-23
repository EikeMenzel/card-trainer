package com.service.userservice.service;

import com.service.userservice.services.EmailValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class EmailValidatorTest {

    @Test
    void testValidEmail() {
        assertTrue(EmailValidator.validate("test@example.com"));
        assertTrue(EmailValidator.validate("user.name@example.co"));
        assertTrue(EmailValidator.validate("user123@subdomain.example.org"));
        assertTrue(EmailValidator.validate("john_doe123@example-domain.com"));
        assertTrue(EmailValidator.validate("user+123@company.org"));
        assertTrue(EmailValidator.validate("email@valid-email.co"));
        assertTrue(EmailValidator.validate("user@domain-with-hyphen.com"));
        assertTrue(EmailValidator.validate("first.last@long-domain-name.org"));
        assertTrue(EmailValidator.validate("user123@sub.subdomain.example.org"));
    }

    @Test
    void testInvalidEmail() {
        assertFalse(EmailValidator.validate("invalid-email"));
        assertFalse(EmailValidator.validate("user@.example.com"));
        assertFalse(EmailValidator.validate("user@.com"));
        assertFalse(EmailValidator.validate("user@ex ample.com"));
        assertFalse(EmailValidator.validate("missing-at-sign-example.com"));
        assertFalse(EmailValidator.validate("user@dot-at-end."));
        assertFalse(EmailValidator.validate("user@domain+invalid-char.com"));
        assertFalse(EmailValidator.validate("user@inva%lid-char.com"));
        assertFalse(EmailValidator.validate("user@dot-before-at@domain.com"));
        assertFalse(EmailValidator.validate("user@.subdomain.example.org"));

    }

    @Test
    void testMaxLengthEmail() {
        String maxLengthEmailTest = "c".repeat(52) + "@example.com";
        assertTrue(EmailValidator.validate(maxLengthEmailTest));
    }

    @Test
    void testEmailExceedingMaxLength() {
        String exceedingMaxLengthEmail = "a".repeat(53) + "@example.com";
        assertFalse(EmailValidator.validate(exceedingMaxLengthEmail));
    }

    @Test
    void testNullEmail() {
        assertFalse(EmailValidator.validate(null));
    }

    @Test
    void testEmptyEmail() {
        assertFalse(EmailValidator.validate(""));
    }
}