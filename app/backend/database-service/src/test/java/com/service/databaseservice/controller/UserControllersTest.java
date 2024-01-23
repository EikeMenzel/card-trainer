package com.service.databaseservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.databaseservice.payload.out.UserDTO;
import com.service.databaseservice.payload.out.UserDailyReminderDTO;
import com.service.databaseservice.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class)
@ContextConfiguration(classes = {UserController.class, ObjectMapper.class})
@TestPropertySource(locations = "classpath:application.properties")
@WebAppConfiguration
class UserControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;

    @MockBean
    private UserTokenService userTokenService;
    @InjectMocks
    private UserController userController;

    @Mock
    private ObjectMapper objectMapper;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void whenGetUserEmail_ValidUserId_thenReturnsOk() throws Exception {
        when(userService.getUserEmailById(1L)).thenReturn(Optional.of("user@example.com"));

        mockMvc.perform(get("/api/v1/db/users/1/email")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("user@example.com"));
    }

    @Test
    void whenGetUserEmail_UserNotFound_thenReturnsNotFound() throws Exception {
        when(userService.getUserEmailById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/db/users/1/email")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetIsUserVerified_UserVerified_thenReturnsOk() throws Exception {
        when(userService.isUserVerified(1L)).thenReturn(true);

        mockMvc.perform(get("/api/v1/db/users/1/verified")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void whenGetIsUserVerified_UserNotVerified_thenReturnsNotFound() throws Exception {
        when(userService.isUserVerified(1L)).thenReturn(false);

        mockMvc.perform(get("/api/v1/db/users/1/verified")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetUserFromEmail_ValidEmail_thenReturnsOk() throws Exception {
        when(userService.getUserByEmail("user@example.com")).thenReturn(Optional.of(new UserDTO()));

        mockMvc.perform(get("/api/v1/db/users/emails/user@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetUserFromEmail_UserNotFound_thenReturnsNotFound() throws Exception {
        when(userService.getUserByEmail("user@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/db/users/emails/user@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetIdFromUserEmail_ValidEmail_thenReturnsOk() throws Exception {
        when(userService.getUserIdFromEmail("user@example.com")).thenReturn(Optional.of(1L));

        mockMvc.perform(get("/api/v1/db/users/emails/user@example.com/id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void whenGetIdFromUserEmail_EmailNotFound_thenReturnsNotFound() throws Exception {
        when(userService.getUserIdFromEmail("user@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/db/users/emails/user@example.com/id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDoesEmailExist_EmailExists_thenReturnsOk() throws Exception {
        when(userService.doesEmailExist("user@example.com")).thenReturn(true);

        mockMvc.perform(get("/api/v1/db/users/emails/user@example.com/exists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void whenDoesEmailExist_EmailNotExists_thenReturnsNotFound() throws Exception {
        when(userService.doesEmailExist("user@example.com")).thenReturn(false);
        mockMvc.perform(get("/api/v1/db/users/emails/user@example.com/exists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetEmailsOfUsersWithDailyLearnReminder_ThenReturnsOk() throws Exception {
        List<UserDailyReminderDTO> reminders = Arrays.asList(new UserDailyReminderDTO("X", "user1@example.com", "en"), new UserDailyReminderDTO("Y", "user2@example.com", "de"));
        when(userService.getEmailsOfUsersWithDailyLearnReminder()).thenReturn(reminders);

        mockMvc.perform(get("/api/v1/db/users/emails")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
