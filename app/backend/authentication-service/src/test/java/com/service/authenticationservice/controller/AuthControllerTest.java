package com.service.authenticationservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.authenticationservice.payload.inc.*;
import com.service.authenticationservice.payload.out.UpdatePasswordDTOUnauthorized;
import com.service.authenticationservice.security.WebSecurityConfig;
import com.service.authenticationservice.security.jwt.JwtUtils;
import com.service.authenticationservice.security.services.UserDetailsImpl;
import com.service.authenticationservice.security.services.UserDetailsServiceImpl;
import com.service.authenticationservice.services.DbQueryService;
import com.service.authenticationservice.services.EmailQueryService;
import com.service.authenticationservice.services.EmailValidator;
import com.service.authenticationservice.services.PasswordSecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AuthController.class)
@ContextConfiguration(classes = {WebSecurityConfig.class, UserDetailsServiceImpl.class, AuthController.class, AuthenticationManager.class, JwtUtils.class, PasswordSecurityService.class, EmailQueryService.class, PasswordEncoder.class, EmailValidator.class, DbQueryService.class, EmailQueryService.class, RestTemplate.class})
@TestPropertySource(locations = "classpath:application.properties")
@Import({TestConfig.class})
@AutoConfigureMockMvc
@WebAppConfiguration
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RainbowListDTO rainbowListDTO;

    @Autowired
    private PasswordSecurityService passwordSecurityService;

    @MockBean
    private DbQueryService dbQueryService;

    @MockBean
    private EmailQueryService emailQueryService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    private String convertToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    //tests register
    @Test
    void whenRegisterUser_Successful_thenReturnsStatusCreated() throws Exception {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("validUsername", "validEmail@example.com", "StrongPassword12!3");
        String jsonRequest = convertToJson(registerRequestDTO);

        when(dbQueryService.doesUserWithEmailExist(anyString())).thenReturn(Optional.empty());
        when(dbQueryService.saveUser(any(UserDTO.class))).thenReturn(HttpStatus.CREATED);

        Mockito.when(dbQueryService.saveUser(any()))
                .thenReturn(HttpStatus.CREATED);

        mockMvc.perform(post("/api/v1/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void whenRegisterUser_WithShortUsername_thenReturnsBadRequest() throws Exception {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("usr", "validEmail@example.com", "StrongPassword12!3");
        String jsonRequest = convertToJson(registerRequestDTO);

        mockMvc.perform(post("/api/v1/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }
    @Test
    void whenRegisterUser_WithLongUsername_thenReturnsBadRequest() throws Exception {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("usr".repeat(10) + "a", "validEmail@example.com", "StrongPassword12!3");
        String jsonRequest = convertToJson(registerRequestDTO);

        mockMvc.perform(post("/api/v1/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenRegisterUser_WithInvalidEmail_thenReturnsBadRequest() throws Exception {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("validUsername", "invalidEmail", "StrongPassword12!3");
        String jsonRequest = convertToJson(registerRequestDTO);

        mockMvc.perform(post("/api/v1/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error, invalid Email"));
    }

    @Test
    void whenRegisterUser_WithCompromisedPassword_thenReturnsBadRequest() throws Exception {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("validUsername", "validEmail@example.com", "CompromisedPass");
        String jsonRequest = convertToJson(registerRequestDTO);

        doReturn(true).when(mock(PasswordSecurityService.class)).checkPasswordIsInRainbowTable(any());

        mockMvc.perform(post("/api/v1/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenRegisterUser_EmailAlreadyExists_thenReturnsConflict() throws Exception {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("validUsername", "test@example.com", "StrongPassword1!23");
        String jsonRequest = convertToJson(registerRequestDTO);

        when(dbQueryService.doesUserWithEmailExist(any())).thenReturn(Optional.of(true));

        mockMvc.perform(post("/api/v1/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Error, Email already exists"));
    }

    //Test Login
    @Test
    void whenLoginUser_WithCorrectCredentialsAndVerifiedUser_thenReturnsOk() throws Exception {
        LoginDTO loginDTO = new LoginDTO("validEmail@example.com", "StrongPassword12!3");
        String jsonRequest = convertToJson(loginDTO);

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
        UserDetailsImpl userDetailsMock = Mockito.mock(UserDetailsImpl.class);
        when(auth.getPrincipal()).thenReturn(userDetailsMock);

        when(dbQueryService.getVerificationStateUser(anyLong())).thenReturn(Optional.of(true));

        ResponseCookie jwtCookie = ResponseCookie.from("JWT", "token").build();
        when(jwtUtils.generateJwtCookie(any(UserDetailsImpl.class))).thenReturn(jwtCookie);

        mockMvc.perform(post("/api/v1/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void whenLoginUser_WithInvalidCredentials_thenReturnsUnauthorized() throws Exception {
        LoginDTO loginDTO = new LoginDTO("invalidEmail@example.com", "WrongPassword");
        String jsonRequest = convertToJson(loginDTO);

        doThrow(new BadCredentialsException("Bad credentials")).when(authenticationManager).authenticate(any());

        mockMvc.perform(post("/api/v1/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());
    }

    //Test verifyUserEmail
    @Test
    void whenVerifyUserEmail_WithEmailAlreadyVerified_thenReturnsConflict() throws Exception {
        String token = "alreadyVerifiedToken";

        when(dbQueryService.setVerificationStateToTrue(token)).thenReturn(HttpStatus.CONFLICT);

        mockMvc.perform(get("/api/v1/email/verify/" + token))
                .andExpect(status().isConflict());
    }

    @Test
    void whenVerifyUserEmail_WithInvalidToken_thenReturnsBadRequest() throws Exception {
        String invalidToken = "invalidToken123";

        when(dbQueryService.setVerificationStateToTrue(invalidToken)).thenReturn(HttpStatus.BAD_REQUEST);

        mockMvc.perform(get("/api/v1/email/verify/" + invalidToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenVerifyUserEmail_WithErrorDuringVerification_thenReturnsInternalServerError() throws Exception {
        String token = "errorToken";

        when(dbQueryService.setVerificationStateToTrue(token)).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

        mockMvc.perform(get("/api/v1/email/verify/" + token))
                .andExpect(status().isInternalServerError());
    }


    //Test UpdateUserPassword
    @Test
    void whenUpdateUserPassword_Successful_thenReturnsNoContent() throws Exception {
        Long userId = 1L;
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO("NewStrongPassword12!3");
        String jsonRequest = convertToJson(updatePasswordDTO);

        when(dbQueryService.updateUserPassword(eq(userId), any(UpdatePasswordDTO.class))).thenReturn(HttpStatus.NO_CONTENT);

        mockMvc.perform(put("/api/v1/password")
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenUpdateUserPassword_WithCompromisedPassword_thenReturnsBadRequest() throws Exception {
        Long userId = 1L;
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO("CompromisedPass");
        String jsonRequest = convertToJson(updatePasswordDTO);

        doReturn(true).when(mock(PasswordSecurityService.class)).checkPasswordIsInRainbowTable(any());

        mockMvc.perform(put("/api/v1/password")
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    //tests sendPasswordResetMail
    @Test
    void whenSendPasswordResetMail_Successful_thenReturnsAccepted() throws Exception {
        MailDTO mailDTO = new MailDTO("validEmail@example.com");
        String jsonRequest = convertToJson(mailDTO);

        when(dbQueryService.getUserIdByEmail(any())).thenReturn(Optional.of(1L));

        mockMvc.perform(post("/api/v1/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isAccepted());
    }

    @Test
    void whenSendPasswordResetMail_WithInvalidEmail_thenReturnsBadRequest() throws Exception {
        MailDTO mailDTO = new MailDTO("invalidEmail");
        String jsonRequest = convertToJson(mailDTO);

        mockMvc.perform(post("/api/v1/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    //Tests updateUserPasswordUnauthenticated
    @Test
    void whenUpdateUserPasswordUnauthenticated_Successful_thenReturnsNoContent() throws Exception {
        PasswordResetDTO passwordResetDTO = new PasswordResetDTO("validEmail@example.com", "ValidToken123", "NewStrongPassword12!3");
        String jsonRequest = convertToJson(passwordResetDTO);

        when(dbQueryService.isTokenValid(passwordResetDTO.token())).thenReturn(true);
        when(dbQueryService.getUserIdByEmail(passwordResetDTO.email())).thenReturn(Optional.of(1L));
        when(dbQueryService.updateUserPasswordUnauthorized(eq(1L), any(UpdatePasswordDTOUnauthorized.class))).thenReturn(HttpStatus.NO_CONTENT);

        mockMvc.perform(put("/api/v1/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenUpdateUserPasswordUnauthenticated_WithInvalidToken_thenReturnsBadRequest() throws Exception {
        PasswordResetDTO passwordResetDTO = new PasswordResetDTO("validEmail@example.com", "InvalidToken", "NewStrongPassword12!3");
        String jsonRequest = convertToJson(passwordResetDTO);

        when(dbQueryService.isTokenValid(passwordResetDTO.token())).thenReturn(false);

        mockMvc.perform(put("/api/v1/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

}

@TestConfiguration
class TestConfig {
    @Bean
    public RainbowListDTO queryRainbowTable() {
        return mock(RainbowListDTO.class);
    }

}
