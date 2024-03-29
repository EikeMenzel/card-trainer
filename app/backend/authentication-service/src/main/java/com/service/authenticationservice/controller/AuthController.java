package com.service.authenticationservice.controller;

import com.service.authenticationservice.model.MailType;
import com.service.authenticationservice.payload.inc.*;
import com.service.authenticationservice.payload.out.MessageResponseDTO;
import com.service.authenticationservice.payload.out.UpdatePasswordDTOUnauthorized;
import com.service.authenticationservice.payload.out.UserInfoResponseDTO;
import com.service.authenticationservice.security.jwt.JwtUtils;
import com.service.authenticationservice.security.services.UserDetailsImpl;
import com.service.authenticationservice.services.DbQueryService;
import com.service.authenticationservice.services.EmailQueryService;
import com.service.authenticationservice.services.EmailValidator;
import com.service.authenticationservice.services.PasswordSecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    private final boolean skipVerify;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static final String ERROR_PASSWORD_IS_A_BLOCKED_PASSWORD = "Error, Password is a blocked password";
    private static final String PASSWORD_CONSTRAINTS_MESSAGE = "Error, Please make sure you are using at least 1x digit, 1x capitalized and 1x lower-case letter and at least 1x symbol from the following pool: ~`! @#$%^&*()_-+={[}]|:;<,>.?/ and the password needs to be between 8 and 72 characters";
    private final String gatewayPath;
    @SuppressWarnings("java:S107")
    public AuthController(PasswordSecurityService passwordSecurityService, DbQueryService dbQueryService, EmailQueryService emailQueryService, PasswordEncoder encoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, @Value("${email.skip.verify}") String skipVerify, @Value("${gateway.path}") String gatewayPath) {
        this.skipVerify = skipVerify != null && skipVerify.equalsIgnoreCase("true");
        this.passwordSecurityService = passwordSecurityService;
        this.dbQueryService = dbQueryService;
        this.emailQueryService = emailQueryService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.gatewayPath = gatewayPath;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Endpoint to register a new user with username, password, and email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid username, password or email format",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflict - Email already exists",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Problem occurred during the registration process",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    public ResponseEntity<MessageResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        if (registerRequest.username().length() < 4 || registerRequest.username().length() > 30) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO(2, "Error, Username needs to be between 4 and 30 characters"));
        }

        if (passwordSecurityService.checkPasswordIsInRainbowTable(registerRequest.password())) //contains check, returns true if the password is in the table
            return ResponseEntity.badRequest().body(new MessageResponseDTO(3, ERROR_PASSWORD_IS_A_BLOCKED_PASSWORD));

        if (!passwordSecurityService.checkPasswordSecurity(registerRequest.password())) { //false if requirements not meet
            return ResponseEntity.badRequest().body(new MessageResponseDTO(3, PASSWORD_CONSTRAINTS_MESSAGE));
        }

        if (!EmailValidator.validate(registerRequest.email())) { //checks for Regex of an email + email-length
            return ResponseEntity.badRequest().body(new MessageResponseDTO(1, "Error, invalid Email"));
        }

        if (dbQueryService.doesUserWithEmailExist(registerRequest.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponseDTO(1, "Error, Email already exists"));
        }

        HttpStatusCode resultSaveUser = dbQueryService.saveUser(new UserDTO(registerRequest.username(), registerRequest.email(), encoder.encode(registerRequest.password()), skipVerify));
        if (skipVerify) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        if (resultSaveUser == HttpStatus.CREATED) { //send VerificationMail
            Optional<Long> userId = dbQueryService.getUserIdByEmail(registerRequest.email());
            if (userId.isEmpty()) //Information could not be fetched from database
            {
                return ResponseEntity.internalServerError().body(new MessageResponseDTO(-1, "Error, some problem occurred"));
            }

            emailQueryService.sendEmail(userId.get(), MailType.VERIFICATION);
        } else {
            return ResponseEntity.internalServerError().body(new MessageResponseDTO(-1, "Error, some problem occurred"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Endpoint to authenticate and log in a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication failed",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
    })
    @SuppressWarnings("java:S1452") // needs to be a wildcard, because multiple different objects are handled
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if (dbQueryService.getVerificationStateUser(userDetails.id()).isEmpty())
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponseDTO(4, "Authentication failed: The user is not verified"));

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new UserInfoResponseDTO(userDetails.id()));
        } catch (AuthenticationException e) {
            logger.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponseDTO(-1, "Authentication failed: " + e.getMessage()));
        }
    }

    @GetMapping("/email/verify/{token}") //Needs to be a getMapping, since mail-clients block javascript code. PUT would not be possible.
    @Operation(summary = "Verify User Email", description = "Endpoint to verify a user's email using a verification token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "308", description = "Permanent Redirect - Email verification successful, redirect to success page"),
            @ApiResponse(responseCode = "409", description = "Conflict - Email is already verified",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Token is not acceptable",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - An error occurred",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    public ResponseEntity<MessageResponseDTO> verifyUserEmail(@PathVariable String token) {
        var httpStatusCode = dbQueryService.setVerificationStateToTrue(token);
        if (httpStatusCode.equals(HttpStatus.NO_CONTENT)) {
            return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).location(URI.create(gatewayPath + "/verify-successful")).build();
        } else if (httpStatusCode.equals(HttpStatus.CONFLICT)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponseDTO(1, "Email is already verified"));
        } else if (httpStatusCode.equals(HttpStatus.BAD_REQUEST)) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO(5, "Token is not acceptable"));
        } else if (httpStatusCode.is5xxServerError()) {
            return ResponseEntity.internalServerError().body(new MessageResponseDTO(-1, "An error has occurred, please try again later"));
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @PutMapping("/password")
    @Operation(summary = "Update User Password", description = "Endpoint to update a user's password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Password does not meet security requirements or is blocked",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - An error occurred",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    public ResponseEntity<MessageResponseDTO> updateUserPassword(@RequestHeader Long userId, @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        if (passwordSecurityService.checkPasswordIsInRainbowTable(updatePasswordDTO.password())) //contains check, returns true if the password is in the table
            return ResponseEntity.badRequest().body(new MessageResponseDTO(3, ERROR_PASSWORD_IS_A_BLOCKED_PASSWORD));

        if (!passwordSecurityService.checkPasswordSecurity(updatePasswordDTO.password())) { //false if requirements not meet
            return ResponseEntity.badRequest().body(new MessageResponseDTO(3, PASSWORD_CONSTRAINTS_MESSAGE));
        }

        return ResponseEntity.status(dbQueryService.updateUserPassword(userId, new UpdatePasswordDTO(encoder.encode(updatePasswordDTO.password())))).build();
    }

    @PostMapping("/password/reset")
    @Operation(summary = "Send Password Reset Email", description = "Endpoint to send a password reset email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Password reset email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid email format",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - An error occurred",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    public ResponseEntity<MessageResponseDTO> sendPasswordResetMail(@Valid @RequestBody MailDTO mailDTO) {
        if(!EmailValidator.validate(mailDTO.email()))
            return ResponseEntity.badRequest().body(new MessageResponseDTO(1, "Error, invalid Email"));

        Optional<Long> optionalUserId = dbQueryService.getUserIdByEmail(mailDTO.email());
        optionalUserId.ifPresent(userId -> emailQueryService.sendEmail(userId, MailType.PASSWORD_RESET));

        return ResponseEntity.accepted().build();
    }

    @PutMapping("/password/reset")
    @Operation(summary = "Update User Password Unauthenticated", description = "Endpoint to update a user's password without authentication. - token is from POST /password/reset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid password or token",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - An error occurred",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @SuppressWarnings("java:S5411") // Boolean is already primitive at @isTokenValid
    public ResponseEntity<MessageResponseDTO> updateUserPasswordUnauthenticated(@Valid @RequestBody PasswordResetDTO passwordResetDTO) {
        if (passwordSecurityService.checkPasswordIsInRainbowTable(passwordResetDTO.password())) //contains check, returns true if the password is in the table
            return ResponseEntity.badRequest().body(new MessageResponseDTO(3, ERROR_PASSWORD_IS_A_BLOCKED_PASSWORD));

        if (!passwordSecurityService.checkPasswordSecurity(passwordResetDTO.password())) { //false if requirements not meet
            return ResponseEntity.badRequest().body(new MessageResponseDTO(3, PASSWORD_CONSTRAINTS_MESSAGE));
        }

        if (!dbQueryService.isTokenValid(passwordResetDTO.token())) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO(5, "Token is invalid, expired or used"));
        }

        Optional<Long> userIdOptional = dbQueryService.getUserIdByEmail(passwordResetDTO.email());
        return userIdOptional.<ResponseEntity<MessageResponseDTO>>map(aLong -> ResponseEntity.status(dbQueryService.updateUserPasswordUnauthorized(aLong, new UpdatePasswordDTOUnauthorized(passwordResetDTO.token(), encoder.encode(passwordResetDTO.password())))).build()).orElseGet(() -> ResponseEntity.badRequest().body(new MessageResponseDTO(-1, "No user found with this E-Mail-Address")));
    }

}