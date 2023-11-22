package com.service.authenticationservice.services;

import com.service.authenticationservice.payload.inc.RainbowListDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PasswordSecurityServiceTest {

    private PasswordSecurityService passwordSecurityService;
    private RainbowListDTO rainbowListDTO;

    @BeforeEach
    void setUp() {
        rainbowListDTO = Mockito.mock(RainbowListDTO.class);
        passwordSecurityService = new PasswordSecurityService(rainbowListDTO);
    }

    @Test
    void testCheckPasswordSecurity_ValidPassword() {
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123!"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123~"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123`"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123!"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123@"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123#"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123$"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123%"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123^"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123&"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123*"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123()"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123_-"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123+"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123={[}]"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123|"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123:"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123;<,>.?/"));
    }

    @Test
    void testCheckPasswordSecurity_TooShort() {
        assertFalse(passwordSecurityService.checkPasswordSecurity("Sho1!"));
    }

    @Test
    void testCheckPasswordSecurity_MinimumLength() {
        assertTrue(passwordSecurityService.checkPasswordSecurity("Shor1!"));
    }
    @Test
    void testCheckPasswordSecurity_MaximumLength() {
        assertTrue(passwordSecurityService.checkPasswordSecurity("t1!"+"W".repeat(69)));
    }

    @Test
    void testCheckPasswordSecurity_ToLong() {
        assertFalse(passwordSecurityService.checkPasswordSecurity("t1!"+"W".repeat(70)));
    }

    @Test
    void testCheckPasswordSecurity_NoDigit() {
        assertFalse(passwordSecurityService.checkPasswordSecurity("NoDigitSymbol!"));
    }

    @Test
    void testCheckPasswordSecurity_NoSymbol() {
        assertFalse(passwordSecurityService.checkPasswordSecurity("NoSymbol123"));
    }

    @Test
    void testCheckPasswordSecurity_UnknownChar() {
        assertFalse(passwordSecurityService.checkPasswordSecurity("UnknownChar×‼123"));
    }

    @Test
    void testCheckPasswordSecurity_UpperCaseMissing() {
        assertFalse(passwordSecurityService.checkPasswordSecurity("mismatchedcase1!".toLowerCase()));
    }

    @Test
    void testCheckPasswordSecurity_LowerCaseMissing() {
        assertFalse(passwordSecurityService.checkPasswordSecurity("mismatchedcase1!".toUpperCase()));
    }

    @Test
    void testCheckPasswordIsRainbow_InRainbowTable() {
        Mockito.when(rainbowListDTO.rainBowList()).thenReturn(new HashSet<>(List.of("password1", "password2", "password3")));
        assertTrue(passwordSecurityService.checkPasswordIsRainbow("password2"));
    }

    @Test
    void testCheckPasswordIsRainbow_NotInRainbowTable() {
        Mockito.when(rainbowListDTO.rainBowList()).thenReturn(new HashSet<>(List.of("password1", "password2", "password3")));
        assertFalse(passwordSecurityService.checkPasswordIsRainbow("notInRainbowTable"));
    }
}
