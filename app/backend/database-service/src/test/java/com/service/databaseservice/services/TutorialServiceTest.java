package com.service.databaseservice.services;

import com.service.databaseservice.model.User;
import com.service.databaseservice.model.tutorial.TutorialPage;
import com.service.databaseservice.model.tutorial.TutorialType;
import com.service.databaseservice.model.tutorial.UserTutorialPage;
import com.service.databaseservice.repository.UserRepository;
import com.service.databaseservice.repository.tutorial.TutorialTypeRepository;
import com.service.databaseservice.repository.tutorial.UserTutorialPageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TutorialService.class)
class TutorialServiceTest {
    @MockBean
    private TutorialTypeRepository tutorialTypeRepository;

    @MockBean
    private UserTutorialPageRepository userTutorialPageRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private TutorialService tutorialService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTutorialPageSuccess() {
        Long userId = 1L;
        TutorialPage tutorialPage = TutorialPage.DECK_VIEW;
        User user = new User();
        TutorialType tutorialType = new TutorialType();

        when(userRepository.getUserById(userId)).thenReturn(Optional.of(user));
        when(tutorialTypeRepository.findTutorialTypeByType(tutorialPage.name())).thenReturn(Optional.of(tutorialType));

        boolean result = tutorialService.saveTutorialPage(tutorialPage, userId);

        assertTrue(result);
        verify(userTutorialPageRepository).save(any(UserTutorialPage.class));
    }

    @Test
    void testDoesUserHasTutorialPageExists() {
        Long userId = 1L;
        TutorialPage tutorialPage = TutorialPage.DECK_VIEW;

        when(userTutorialPageRepository.existsUserTutorialPageByUserIdAndTutorialTypeType(userId, tutorialPage.name())).thenReturn(true);

        boolean result = tutorialService.doesUserHasTutorialPage(tutorialPage, userId);

        assertTrue(result);
    }

    @Test
    void testSaveTutorialPageUserNotFound() {
        Long userId = 1L;
        TutorialPage tutorialPage = TutorialPage.EDIT_DECK;

        when(userRepository.getUserById(userId)).thenReturn(Optional.empty());

        boolean result = tutorialService.saveTutorialPage(tutorialPage, userId);

        assertFalse(result);
        verify(userTutorialPageRepository, never()).save(any(UserTutorialPage.class));
    }

    @Test
    void testSaveTutorialPageTutorialTypeNotFound() {
        Long userId = 1L;
        TutorialPage tutorialPage = TutorialPage.DECK_VIEW;
        User user = new User();

        when(userRepository.getUserById(userId)).thenReturn(Optional.of(user));
        when(tutorialTypeRepository.findTutorialTypeByType(tutorialPage.name())).thenReturn(Optional.empty());

        boolean result = tutorialService.saveTutorialPage(tutorialPage, userId);

        assertFalse(result);
        verify(userTutorialPageRepository, never()).save(any(UserTutorialPage.class));
    }

    @Test
    void testSaveTutorialPageExceptionHandling() {
        Long userId = 1L;
        TutorialPage tutorialPage = TutorialPage.DECK_VIEW;
        User user = new User();
        TutorialType tutorialType = new TutorialType();

        when(userRepository.getUserById(userId)).thenReturn(Optional.of(user));
        when(tutorialTypeRepository.findTutorialTypeByType(tutorialPage.name())).thenReturn(Optional.of(tutorialType));
        doThrow(new RuntimeException("Database error")).when(userTutorialPageRepository).save(any(UserTutorialPage.class));

        boolean result = tutorialService.saveTutorialPage(tutorialPage, userId);

        assertFalse(result);
    }
}
