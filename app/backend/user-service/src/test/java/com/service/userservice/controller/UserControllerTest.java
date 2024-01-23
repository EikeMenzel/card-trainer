package com.service.userservice.controller;


import com.service.userservice.payload.inc.UserAccountInformationDTO;
import com.service.userservice.services.DbQueryService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class)
@ContextConfiguration(classes = {UserController.class, DbQueryService.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@WebAppConfiguration
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private DbQueryService dbQueryService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void whenGetAccountInformation_ReturnsOk() throws Exception {
        when(dbQueryService.getAccountInformation(anyLong()))
                .thenReturn(Optional.of(new UserAccountInformationDTO("Some username", "test@example.com", 10, false, "en", 3)));

        mockMvc.perform(get("/api/v1/account")
                        .header("userId", 1))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetAccountInformation_UserNotFound_ReturnsNotFound() throws Exception {
        when(dbQueryService.getAccountInformation(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/account")
                        .header("userId", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenUpdateAccountInformation_ValidData_ReturnsOk() throws Exception {
        when(dbQueryService.updateAccountInformation(anyLong(), any()))
                .thenReturn(Pair.of(Optional.of(new UserAccountInformationDTO("Some username", "test@example.com", 10, false, "en", 3)), HttpStatus.OK));

        mockMvc.perform(put("/api/v1/account")
                        .header("userId", 1)
                        .contentType("application/json")
                        .content("{\"username\": \"newUsername\", \"email\": \"newemail@example.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void whenUpdateAccountInformation_InvalidData_ReturnsBadRequest() throws Exception {
        mockMvc.perform(put("/api/v1/account")
                        .header("userId", -1)
                        .contentType("application/json")
                        .content("{\"username\": \"nu\", \"email\": \"invalidEmail\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUpdateAccountInformation_UserNotFound_ReturnsNotFound() throws Exception {
        when(dbQueryService.updateAccountInformation(anyLong(), any()))
                .thenReturn(Pair.of(Optional.empty(), HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/api/v1/account")
                        .header("userId", 1)
                        .contentType("application/json")
                        .content("{\"username\": \"newUsername\", \"email\": \"newemail@example.com\"}"))
                .andExpect(status().isNotFound());
    }
}
