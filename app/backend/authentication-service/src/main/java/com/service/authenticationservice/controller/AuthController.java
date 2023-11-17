package com.service.authenticationservice.controller;

import com.service.authenticationservice.model.MailType;
import com.service.authenticationservice.payload.inc.RegisterRequestDTO;
import com.service.authenticationservice.payload.inc.UserDTO;
import com.service.authenticationservice.payload.out.MessageResponseDTO;
import com.service.authenticationservice.security.jwt.JwtUtils;
import com.service.authenticationservice.services.DbQueryService;
import com.service.authenticationservice.services.EmailQueryService;
import com.service.authenticationservice.services.EmailValidator;
import com.service.authenticationservice.services.PasswordSecurityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class AuthController {
    private final PasswordSecurityService passwordSecurityService;
    private final DbQueryService dbQueryService;
    private final EmailQueryService emailQueryService;

    private final PasswordEncoder encoder;


    public AuthController(PasswordSecurityService passwordSecurityService, DbQueryService dbQueryService, EmailQueryService emailQueryService, PasswordEncoder encoder) {
        this.passwordSecurityService = passwordSecurityService;
        this.dbQueryService = dbQueryService;
        this.emailQueryService = emailQueryService;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) {

        if(registerRequest.username().length() < 4 || registerRequest.username().length() > 30) {
            return ResponseEntity.badRequest().body("Error, Username is to long or to short");
        }

        if(passwordSecurityService.checkPasswordIsRainbow(registerRequest.password())) //contains check, returns true if the password is in the table
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error, Password is a blocked password"));

        if(!passwordSecurityService.checkPasswordSecurity(registerRequest.password())) { //false if requirements not meet
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error, Please make sure you are using at least 1x digit, 1x capitalized and 1x lower-case letter and at least 1x symbol from the following pool: ~`! @#$%^&*()_-+={[}]|:;<,>.?/"));
        }

        if(!EmailValidator.validate(registerRequest.email())) { //checks for Regex of an email + email-length
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error, invalid Email"));
        }

        if(dbQueryService.doesUserWithEmailExist(registerRequest.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error, Email already exists");
        }

        HttpStatusCode resultSaveUser = dbQueryService.saveUser(new UserDTO(registerRequest.username(), registerRequest.email(), encoder.encode(registerRequest.password())));
        if(resultSaveUser == HttpStatus.CREATED) { //send VerificationMail
            Optional<Long> userId = dbQueryService.getUserIdByEmail(registerRequest.email());
            if(userId.isEmpty()) //Information could not be fetched from database
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

            emailQueryService.sendEmail(userId.get(), MailType.VERIFICATION);
        } else {
            return ResponseEntity.internalServerError().body("Error, some problem occurred");
        }

        return ResponseEntity.ok().build();
    }
}