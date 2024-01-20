package com.service.userservice.controller;

import com.service.userservice.payload.inc.TutorialPage;
import com.service.userservice.services.DbQueryService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TutorialController.class)
@ContextConfiguration(classes = {TutorialController.class, DbQueryService.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@WebAppConfiguration
class TutorialControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private DbQueryService dbQueryService;
    @InjectMocks
    private TutorialController tutorialController;

    @Test
    void testDoesUserHaveTutorialPageExists() throws Exception {
        Long userId = 1L;
        TutorialPage tutorialPage = TutorialPage.PEEK_CARD_VIEW;

        when(dbQueryService.doesUserHaveTutorialPage(userId, tutorialPage)).thenReturn(true);

        mockMvc.perform(get("/api/v1/tutorials/" + tutorialPage.name())
                        .header("userId", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDoesUserHaveTutorialPageNotExists() throws Exception {
        Long userId = 1L;
        TutorialPage tutorialPage = TutorialPage.PEEK_CARD_VIEW;

        when(dbQueryService.doesUserHaveTutorialPage(userId, tutorialPage)).thenReturn(false);

        mockMvc.perform(get("/api/v1/tutorials/" + tutorialPage.name())
                        .header("userId", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveUserTutorialPageSuccess() throws Exception {
        Long userId = 1L;
        TutorialPage tutorialPage = TutorialPage.PEEK_CARD_VIEW;

        when(dbQueryService.saveTutorialPage(userId, tutorialPage)).thenReturn(HttpStatus.CREATED);

        mockMvc.perform(post("/api/v1/tutorials/" + tutorialPage.name())
                        .header("userId", userId))
                .andExpect(status().isCreated());
    }

    @Test
    void testSaveUserTutorialPageFailure() throws Exception {
        Long userId = 1L;
        TutorialPage tutorialPage = TutorialPage.PEEK_CARD_VIEW;

        when(dbQueryService.saveTutorialPage(userId, tutorialPage)).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

        mockMvc.perform(post("/api/v1/tutorials/" + tutorialPage.name())
                        .header("userId", userId))
                .andExpect(status().isInternalServerError());
    }

}
