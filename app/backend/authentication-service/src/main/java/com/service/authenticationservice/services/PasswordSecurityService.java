package com.service.authenticationservice.services;

import com.service.authenticationservice.payload.inc.RainbowListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordSecurityService {
    private final RainbowListDTO queryRainbowTable;

    @Autowired
    public PasswordSecurityService(RainbowListDTO queryRainbowTable) {
        this.queryRainbowTable = queryRainbowTable;
    }

    public boolean checkPasswordSecurity(String password) {
        var minLength = 6;
        var maxLength = 72;

        if (password.length() < minLength || password.length() > maxLength) {
            return false;
        }

        var hasDigit = false;
        var hasSymbole = false;
        var hasUnknownChar = false;
        var hasUppercase = !password.equals(password.toLowerCase());
        var hasLowercase = !password.equals(password.toUpperCase());

        for (char c: password.toCharArray()) {
            if (Character.isLetter(c)) continue;

            if (Character.isDigit(c)) {
                hasDigit = true;
                continue;
            }

            var allowedSymbols = "~`! @#$%^&*()_-+={[}]|:;<,>.?/";
            if(allowedSymbols.indexOf(c) != -1)
                hasSymbole = true;
            else
                hasUnknownChar = true;
        }

        return hasDigit && hasUppercase && hasLowercase && hasSymbole && !hasUnknownChar;
    }

    public boolean checkPasswordIsRainbow(String password) {
        return queryRainbowTable.rainBowList().contains(password);
    }
}