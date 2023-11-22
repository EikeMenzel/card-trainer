package com.service.authenticationservice.controller;

import com.service.authenticationservice.model.MailType;
import com.service.authenticationservice.payload.inc.LoginDTO;
import com.service.authenticationservice.payload.inc.RegisterRequestDTO;
import com.service.authenticationservice.payload.inc.UserDTO;
import com.service.authenticationservice.payload.out.MessageResponseDTO;
import com.service.authenticationservice.payload.out.UserInfoResponseDTO;
import com.service.authenticationservice.security.jwt.JwtUtils;
import com.service.authenticationservice.security.services.UserDetailsImpl;
import com.service.authenticationservice.services.DbQueryService;
import com.service.authenticationservice.services.EmailQueryService;
import com.service.authenticationservice.services.EmailValidator;
import com.service.authenticationservice.services.PasswordSecurityService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(PasswordSecurityService passwordSecurityService, DbQueryService dbQueryService, EmailQueryService emailQueryService, PasswordEncoder encoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.passwordSecurityService = passwordSecurityService;
        this.dbQueryService = dbQueryService;
        this.emailQueryService = emailQueryService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) {

        if (registerRequest.username().length() < 4 || registerRequest.username().length() > 30) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error, Username is to long or to short"));
        }

        if (passwordSecurityService.checkPasswordIsRainbow(registerRequest.password())) //contains check, returns true if the password is in the table
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error, Password is a blocked password"));

        if (!passwordSecurityService.checkPasswordSecurity(registerRequest.password())) { //false if requirements not meet
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error, Please make sure you are using at least 1x digit, 1x capitalized and 1x lower-case letter and at least 1x symbol from the following pool: ~`! @#$%^&*()_-+={[}]|:;<,>.?/"));
        }

        if (!EmailValidator.validate(registerRequest.email())) { //checks for Regex of an email + email-length
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error, invalid Email"));
        }

        if (dbQueryService.doesUserWithEmailExist(registerRequest.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponseDTO("Error, Email already exists"));
        }

        HttpStatusCode resultSaveUser = dbQueryService.saveUser(new UserDTO(registerRequest.username(), registerRequest.email(), encoder.encode(registerRequest.password())));
        if (resultSaveUser == HttpStatus.CREATED) { //send VerificationMail
            Optional<Long> userId = dbQueryService.getUserIdByEmail(registerRequest.email());
            if (userId.isEmpty()) //Information could not be fetched from database
                return ResponseEntity.internalServerError().body(new MessageResponseDTO("Error, some problem occurred"));

            emailQueryService.sendEmail(userId.get(), MailType.VERIFICATION);
        } else {
            return ResponseEntity.internalServerError().body(new MessageResponseDTO("Error, some problem occurred"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if (dbQueryService.getVerificationStateUser(userDetails.id()).isEmpty())
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponseDTO("Authentication failed: The user is not verified"));

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new UserInfoResponseDTO(userDetails.id()));
        } catch (AuthenticationException e) {
            logger.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponseDTO("Authentication failed: " + e.getMessage()));
        }
    }

    @GetMapping("/email/verify/{token}") //Needs to be a getMapping, since mail-clients block javascript code. PUT would not be possible.
    public ResponseEntity<?> verifyUserEmail(@PathVariable String token) {
        var httpStatusCode = dbQueryService.updateUserWithToken(token);
        if (httpStatusCode.equals(HttpStatus.NO_CONTENT)) {
            return ResponseEntity.noContent().build();
        } else if (httpStatusCode.equals(HttpStatus.CONFLICT)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponseDTO("Email is already verified"));
        } else if (httpStatusCode.equals(HttpStatus.BAD_REQUEST)) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Token is not acceptable"));
        } else if (httpStatusCode.is5xxServerError()) {
            return ResponseEntity.internalServerError().body(new MessageResponseDTO("An error has occurred, please try again later"));
        }
        return ResponseEntity.unprocessableEntity().build();
    }
}