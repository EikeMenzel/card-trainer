package com.service.databaseservice.controller;

import com.service.databaseservice.services.UserAchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserAchievementsController.class)
@ContextConfiguration(classes = {UserAchievementsController.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@WebAppConfiguration
class UserAchievementsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @InjectMocks
    private UserAchievementsController userAchievementsController;

    @MockBean
    private UserAchievementService userAchievementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        when(userAchievementService.getAllUserAchievementIdsByUserId(anyLong())).thenReturn(Arrays.asList(1L, 2L, 3L));
        when(userAchievementService.doesUserHaveAchievement(anyLong(), anyLong())).thenReturn(true);
        when(userAchievementService.saveUserAchievement(anyLong(), anyLong())).thenReturn(true);
    }

    @Test
    void whenGetAllAchievementsAsIdFromUser_WithValidUserId_thenReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/db/user-achievements/users/1/achievements/ids")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDoesUserHaveAchievement_WithValidUserIdAndAchievementId_thenReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/db/user-achievements/users/1/achievements/2/exists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDoesUserHaveAchievement_WithInvalidUserIdOrAchievementId_thenReturnsNotFound() throws Exception {
        when(userAchievementService.doesUserHaveAchievement(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(get("/api/v1/db/user-achievements/users/1/achievements/2/exists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenSaveUserAchievement_WithValidUserIdAndAchievementId_thenReturnsCreated() throws Exception {
        mockMvc.perform(post("/api/v1/db/user-achievements/users/1/achievements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("3"))
                .andExpect(status().isCreated());
    }

    @Test
    void whenSaveUserAchievement_WithInvalidUserIdOrAchievementId_thenReturnsInternalServerError() throws Exception {
        when(userAchievementService.saveUserAchievement(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(post("/api/v1/db/user-achievements/users/1/achievements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("3"))
                .andExpect(status().isInternalServerError());
    }
}
