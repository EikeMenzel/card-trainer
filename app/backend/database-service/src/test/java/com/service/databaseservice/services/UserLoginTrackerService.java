package com.service.databaseservice.services;

import com.service.databaseservice.model.User;
import com.service.databaseservice.model.achievements.UserLoginTracker;
import com.service.databaseservice.repository.UserRepository;
import com.service.databaseservice.repository.achievements.UserLoginTrackerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserLoginTrackerServiceTest {
    @Mock
    private UserLoginTrackerRepository userLoginTrackerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserLoginTrackerService userLoginTrackerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUserLogin_UserLoggedInToday() {
        Long userId = 1L;
        when(userLoginTrackerRepository.existsByUserId(userId)).thenReturn(true);

        assertEquals(HttpStatus.CONFLICT, userLoginTrackerService.saveUserLogin(userId));
    }

    @Test
    void saveUserLogin_UserNotFound() {
        Long userId = 1L;
        when(userLoginTrackerRepository.existsByUserId(userId)).thenReturn(false);
        when(userRepository.getUserById(userId)).thenReturn(Optional.empty());

        assertEquals(HttpStatus.NOT_FOUND, userLoginTrackerService.saveUserLogin(userId));
    }

    @Test
    void saveUserLogin_Success() {
        Long userId = 1L;
        User user = new User(); // Setup user as needed
        when(userLoginTrackerRepository.existsByUserId(userId)).thenReturn(false);
        when(userRepository.getUserById(userId)).thenReturn(Optional.of(user));

        assertEquals(HttpStatus.CREATED, userLoginTrackerService.saveUserLogin(userId));
        verify(userLoginTrackerRepository).save(any(UserLoginTracker.class));
    }

    @Test
    void saveUserLogin_InternalServerError() {
        Long userId = 1L;
        when(userLoginTrackerRepository.existsByUserId(userId)).thenReturn(false);
        when(userRepository.getUserById(userId)).thenReturn(Optional.of(new User()));
        doThrow(new RuntimeException("Test Exception")).when(userLoginTrackerRepository).save(any(UserLoginTracker.class));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, userLoginTrackerService.saveUserLogin(userId));
    }

    @Test
    void wasUserLoggedInToday_WhenUserHasLoggedInToday() {
        Long userId = 1L;
        when(userLoginTrackerRepository.existsByUserId(userId)).thenReturn(true);

        assertTrue(userLoginTrackerService.wasUserLoggedInToday(userId));
    }

    @Test
    void wasUserLoggedInToday_WhenUserHasNotLoggedInToday() {
        Long userId = 1L;
        when(userLoginTrackerRepository.existsByUserId(userId)).thenReturn(false);

        assertFalse(userLoginTrackerService.wasUserLoggedInToday(userId));
    }
}
