package com.service.databaseservice.controller;

import com.service.databaseservice.payload.out.AchievementDetailsDTO;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AchievementController.class)
@ContextConfiguration(classes = {AchievementController.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@WebAppConfiguration
class AchievementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @InjectMocks
    private AchievementController achievementController;

    @MockBean
    private UserAchievementService userAchievementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        AchievementDetailsDTO achievementDetails = new AchievementDetailsDTO(1L, "Test Achievement", "Description of Test Achievement", false, null);

        when(userAchievementService.getAchievementDetails(anyLong())).thenReturn(Optional.of(achievementDetails));
    }

    @Test
    void whenGetAchievementDetails_WithValidAchievementId_thenReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/db/achievements/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetAchievementDetails_WithInvalidAchievementId_thenReturnsNotFound() throws Exception {
        when(userAchievementService.getAchievementDetails(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/db/achievements/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
