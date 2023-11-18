package com.service.databaseservice.controller;

import com.service.databaseservice.payload.out.UserTokenDTO;
import com.service.databaseservice.services.UserTokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/db/user-token")
public class UserTokenController {
    private final UserTokenService userTokenService;
    public UserTokenController(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }
    @PostMapping("/")
    public ResponseEntity<?> createUserToken(@Valid @RequestBody UserTokenDTO userToken) {
        return userTokenService.createUserToken(userToken)
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.internalServerError().build();
    }
    @PutMapping("/{token}")
    public ResponseEntity<?> updateUserWithToken(@PathVariable String token) {
        if(!userTokenService.isUserTokenValid(token))
            return ResponseEntity.badRequest().build();

        if(userTokenService.isTokenVerificationToken(token)) {
            if(userTokenService.isUserWithTokenVerified(token))
                return ResponseEntity.status(HttpStatus.CONFLICT).build();

            return userTokenService.setUserEmailAsVerified(token)
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.internalServerError().build();

        } // else cases are share token and password reset

        return ResponseEntity.unprocessableEntity().build();
    }
}
