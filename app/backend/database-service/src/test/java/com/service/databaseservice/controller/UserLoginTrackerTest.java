package com.service.databaseservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.databaseservice.services.UserLoginTrackerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserLoginTrackerController.class)
@ContextConfiguration(classes = {UserLoginTrackerController.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@WebAppConfiguration
class UserLoginControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @InjectMocks
    private UserLoginTrackerController userLoginTrackerController;

    @MockBean
    private UserLoginTrackerService userLoginTrackerService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        when(userLoginTrackerService.saveUserLogin(anyLong())).thenReturn(HttpStatus.OK);
        when(userLoginTrackerService.wasUserLoggedInToday(anyLong())).thenReturn(true);
    }

    @Test
    void whenSaveUserLogin_WithValidUserId_thenReturnsOk() throws Exception {
        mockMvc.perform(post("/api/v1/db/users/1/user-login-tracker")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenSaveUserLogin_WithInvalidUserId_thenReturnsInternalServerError() throws Exception {
        when(userLoginTrackerService.saveUserLogin(anyLong())).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

        mockMvc.perform(post("/api/v1/db/users/1/user-login-tracker")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCheckDailyUserLogin_WithValidUserId_thenReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/db/users/1/user-login-tracker")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
