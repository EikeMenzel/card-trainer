package com.service.databaseservice.controller;

import com.service.databaseservice.model.tutorial.TutorialPage;
import com.service.databaseservice.services.TutorialService;
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
@WebMvcTest(controllers = TutorialController.class)
@ContextConfiguration(classes = {TutorialController.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@WebAppConfiguration
class TutorialControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @InjectMocks
    private TutorialController tutorialController;

    @MockBean
    private TutorialService tutorialService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void testDoesUserHaveTutorialPageExists() throws Exception {
        Long userId = 1L;
        TutorialPage tutorialPage = TutorialPage.EDIT_DECK;

        when(tutorialService.doesUserHasTutorialPage(tutorialPage, userId)).thenReturn(true);

        mockMvc.perform(get("/api/v1/db/users/" + userId + "/tutorials/" + tutorialPage.name()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDoesUserHaveTutorialPageNotExists() throws Exception {
        Long userId = 1L;
        TutorialPage tutorialPage = TutorialPage.USER_PROFILE;

        when(tutorialService.doesUserHasTutorialPage(tutorialPage, userId)).thenReturn(false);

        mockMvc.perform(get("/api/v1/db/users/" + userId + "/tutorials/" + tutorialPage.name()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveUserTutorialPageSuccess() throws Exception {
        Long userId = 1L;
        TutorialPage tutorialPage = TutorialPage.LEARN_CARD_VIEW;

        when(tutorialService.saveTutorialPage(tutorialPage, userId)).thenReturn(true);

        mockMvc.perform(post("/api/v1/db/users/" + userId + "/tutorials/" + tutorialPage.name()))
                .andExpect(status().isCreated());
    }

    @Test
    void testSaveUserTutorialPageFailure() throws Exception {
        Long userId = 1L;
        TutorialPage tutorialPage = TutorialPage.LEARN_CARD_VIEW;

        when(tutorialService.saveTutorialPage(tutorialPage, userId)).thenReturn(false);

        mockMvc.perform(post("/api/v1/db/users/" + userId + "/tutorials/" + tutorialPage.name()))
                .andExpect(status().isInternalServerError());
    }

}
