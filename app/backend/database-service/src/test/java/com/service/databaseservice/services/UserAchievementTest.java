package com.service.databaseservice.services;

import com.service.databaseservice.model.Image;
import com.service.databaseservice.model.User;
import com.service.databaseservice.model.achievements.Achievement;
import com.service.databaseservice.model.achievements.UserAchievement;
import com.service.databaseservice.payload.out.AchievementDetailsDTO;
import com.service.databaseservice.repository.ImageRepository;
import com.service.databaseservice.repository.UserRepository;
import com.service.databaseservice.repository.achievements.AchievementRepository;
import com.service.databaseservice.repository.achievements.UserAchievementRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserAchievementService.class)
class UserAchievementTest {
    @MockBean
    private ImageRepository imageRepository;

    @MockBean
    private UserAchievementRepository userAchievementRepository;

    @MockBean
    private AchievementRepository achievementRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserAchievementService userAchievementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUserAchievement_UserOrAchievementNotFound() {
        Long userId = 1L;
        Long achievementId = 1L;

        when(userRepository.getUserById(userId)).thenReturn(Optional.empty());
        assertFalse(userAchievementService.saveUserAchievement(userId, achievementId));

        User user = new User();
        when(userRepository.getUserById(userId)).thenReturn(Optional.of(user));
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());
        assertFalse(userAchievementService.saveUserAchievement(userId, achievementId));
    }

    @Test
    void getAchievementDetails_ExistingAchievement() {
        Long achievementId = 1L;
        Achievement achievement = new Achievement(1L, "X", "x", false, new Image(1L, null, new User()));

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));

        Optional<AchievementDetailsDTO> result = userAchievementService.getAchievementDetails(achievementId);

        assertTrue(result.isPresent());
        assertEquals(achievementId, result.get().achievementId());
    }

    @Test
    void getAchievementDetails_NonExistingAchievement() {
        Long achievementId = 1L;
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

        Optional<AchievementDetailsDTO> result = userAchievementService.getAchievementDetails(achievementId);

        assertFalse(result.isPresent());
    }

    @Test
    void getAllUserAchievementIdsByUserId_UserWithAchievements() {
        User user1 = new User(1L, "username", "email@example.com", "password",false, false, 10, "en");

        List<UserAchievement> userAchievements = List.of(
                new UserAchievement(user1, new Achievement(1L, "X", "x", false, new Image(1L, null, user1))),
                new UserAchievement(user1, new Achievement(2L, "X", "x", false, new Image(2L, null, user1)))
        );
        when(userAchievementRepository.getUserAchievementsByUser_Id(user1.getId())).thenReturn(userAchievements);

        List<Long> result = userAchievementService.getAllUserAchievementIdsByUserId(user1.getId());

        assertEquals(2, result.size());
        assertTrue(result.contains(1L));
    }

    @Test
    void getAllUserAchievementIdsByUserId_UserWithNoAchievements() {
        Long userId = 1L;
        when(userAchievementRepository.getUserAchievementsByUser_Id(userId)).thenReturn(List.of());

        List<Long> result = userAchievementService.getAllUserAchievementIdsByUserId(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllUserAchievementIdsByUserId_UserHasAchievements() {
        Long userId = 1L;
        List<UserAchievement> userAchievements = List.of(
                new UserAchievement(new User(), new Achievement(1L, "Achievement1", "Desc1", false, null)),
                new UserAchievement(new User(), new Achievement(2L, "Achievement2", "Desc2", false, null))
        );

        when(userAchievementRepository.getUserAchievementsByUser_Id(userId)).thenReturn(userAchievements);

        List<Long> result = userAchievementService.getAllUserAchievementIdsByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(1L, 2L)));
    }

    @Test
    void saveUserAchievement_Success() {
        Long userId = 1L;
        Long achievementId = 1L;
        User user = new User();
        Achievement achievement = new Achievement();

        when(userRepository.getUserById(userId)).thenReturn(Optional.of(user));
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));

        assertTrue(userAchievementService.saveUserAchievement(userId, achievementId));
        verify(userAchievementRepository).save(any(UserAchievement.class));
    }

    @Test
    void doesUserHaveAchievement_UserHasAchievement() {
        Long userId = 1L;
        Long achievementId = 1L;

        when(userAchievementRepository.hasUserAchieved(achievementId, userId)).thenReturn(true);

        assertTrue(userAchievementService.doesUserHaveAchievement(userId, achievementId));
    }
}
