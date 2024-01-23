package com.service.achievementservice.controller;

import com.service.achievementservice.payload.AchievementDetailsDTO;
import com.service.achievementservice.services.AchievementService;
import com.service.achievementservice.services.DbQueryService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AchievementController.class)
@TestPropertySource(locations = "classpath:application.properties")
class AchievementControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AchievementService achievementService;

    @MockBean
    private DbQueryService dbQueryService;

    @Test
    void checkDeckAchievements_Success() throws Exception {
        doNothing().when(achievementService).achievementCreationDeck(anyLong());

        mockMvc.perform(post("/api/v1/users/1/achievements/check-deck"))
                .andExpect(status().isOk());

        verify(achievementService, times(1)).achievementCreationDeck(1L);
    }

    @Test
    void checkSessionAchievements_Success() throws Exception {
        doNothing().when(achievementService).achievementSessionDaily(anyLong());
        doNothing().when(achievementService).achievementStateSessions(anyLong());

        mockMvc.perform(post("/api/v1/users/1/achievements/check-session"))
                .andExpect(status().isOk());

        verify(achievementService, times(1)).achievementSessionDaily(1L);
        verify(achievementService, times(1)).achievementStateSessions(1L);
    }

    @Test
    void checkCardsLearnedAchievements_Success() throws Exception {
        doNothing().when(achievementService).achievementCardsLearnedDaily(anyLong());
        doNothing().when(achievementService).achievementCardsLearned(anyLong());

        mockMvc.perform(post("/api/v1/users/1/achievements/check-cards-learned"))
                .andExpect(status().isOk());

        verify(achievementService, times(1)).achievementCardsLearnedDaily(1L);
        verify(achievementService, times(1)).achievementCardsLearned(1L);
    }

    @Test
    void checkDailyLoginAchievement_Success() throws Exception {
        doNothing().when(achievementService).achievementDailyLogin(anyLong());

        mockMvc.perform(post("/api/v1/users/1/achievements/check-daily-login"))
                .andExpect(status().isOk());

        verify(achievementService, times(1)).achievementDailyLogin(1L);
    }

    @Test
    void getAchievementDetails_Success() throws Exception {
        AchievementDetailsDTO achievementDetailsDTO = new AchievementDetailsDTO(1L, "Test", "Desc", false, 1L);
        when(dbQueryService.getAchievementDetails(anyLong())).thenReturn(Optional.of(achievementDetailsDTO));

        mockMvc.perform(get("/api/v1/achievements/1")
                        .header("userId", 1L))
                .andExpect(status().isOk());

        verify(dbQueryService, times(1)).getAchievementDetails(1L);
    }
}
