package com.service.databaseservice.services;

import com.service.databaseservice.model.User;
import com.service.databaseservice.payload.out.UserDTO;
import com.service.databaseservice.payload.out.UserDailyReminderDTO;
import com.service.databaseservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
//@SpringBootTest
@DataJpaTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUserEmailById_ValidUserId() {
        Long userId = 1L;
        User mockUser = new User("testUser", "test@example.com", "password");

        when(userRepository.getUserById(userId)).thenReturn(Optional.of(mockUser));

        Optional<String> userEmail = userService.getUserEmailById(userId);

        assertTrue(userEmail.isPresent());
        assertEquals("test@example.com", userEmail.get());
    }

    @Test
    void testGetUserEmailById_NonExistingUserId() {
        Long userId = 99L;

        when(userRepository.getUserById(userId)).thenReturn(Optional.empty());

        Optional<String> userEmail = userService.getUserEmailById(userId);

        assertFalse(userEmail.isPresent());
    }

    @Test
    void testGetUserEmailById_InvalidUserId() {
        Long invalidUserId = -1L;

        when(userRepository.getUserById(invalidUserId)).thenReturn(Optional.empty());

        Optional<String> userEmail = userService.getUserEmailById(invalidUserId);

        assertFalse(userEmail.isPresent());
    }

    @Test
    void testGetUserIdFromEmail_NonExistingUserEmail() {
        String nonExistingUserEmail = "nonexisting@example.com";

        when(userRepository.getUserByEmail(nonExistingUserEmail)).thenReturn(Optional.empty());

        Optional<Long> userId = userService.getUserIdFromEmail(nonExistingUserEmail);

        assertFalse(userId.isPresent());
    }

    @Test
    void testGetUserIdFromEmail_InvalidUserEmail() {
        when(userRepository.getUserByEmail(null)).thenReturn(Optional.empty());

        Optional<Long> userId = userService.getUserIdFromEmail(null);

        assertFalse(userId.isPresent());
    }

    @Test
    void testDoesEmailExist_ExistingEmail() {
        String existingEmail = "test@example.com";

        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        assertTrue(userService.doesEmailExist(existingEmail));
    }

    @Test
    void testDoesEmailExist_NonExistingEmail() {
        String nonExistingEmail = "nonexisting@example.com";

        when(userRepository.existsByEmail(nonExistingEmail)).thenReturn(false);

        assertFalse(userService.doesEmailExist(nonExistingEmail));
    }
    @Test
    void testDoesEmailExist_InvalidEmail() {
        when(userRepository.existsByEmail(null)).thenReturn(false);

        assertFalse(userService.doesEmailExist(null));
    }

    @Test
    void testIsUserVerifiedWhenUserExistsAndIsVerified() {
        Long userId = 1L;
        when(userRepository.existsByIdAndIsVerifiedTrue(userId)).thenReturn(true);

        assertTrue(userService.isUserVerified(userId));
    }

    @Test
    void testIsUserVerifiedWhenUserDoesNotExist() {
        Long userId = 1L;
        when(userRepository.existsByIdAndIsVerifiedTrue(userId)).thenReturn(false);

        assertFalse(userService.isUserVerified(userId));
    }

    @Test
    void testGetUserFromId_ValidUserId() {
        Long userId = 1L;
        User mockUser = new User("testUser", "test@example.com", "password");

        when(userRepository.getUserById(userId)).thenReturn(Optional.of(mockUser));

        Optional<User> user = userService.getUserFromId(userId);

        assertTrue(user.isPresent());
        assertEquals("testUser", user.get().getUsername());
    }

    @Test
    void testGetUserFromId_NonExistingUserId() {
        Long nonExistingUserId = 99L;

        when(userRepository.getUserById(nonExistingUserId)).thenReturn(Optional.empty());

        Optional<User> user = userService.getUserFromId(nonExistingUserId);

        assertFalse(user.isPresent());
    }

    @Test
    void testGetUserFromId_InvalidUserId() {
        Long invalidUserId = -1L;

        when(userRepository.getUserById(invalidUserId)).thenReturn(Optional.empty());

        Optional<User> user = userService.getUserFromId(invalidUserId);

        assertFalse(user.isPresent());    }

    @Test
    void testCreateUser_ValidUserDTO() {
        UserDTO userDTO = new UserDTO("newUser", "newuser@example.com", "password");

        when(userRepository.save(any(User.class))).thenReturn(new User(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword()));

        boolean result = userService.createUser(userDTO);

        assertTrue(result);
    }

    @Test
    void testCreateUser_ErrorInUserCreation() {
        UserDTO userDTO = new UserDTO("newUser", "newuser@example.com", "password");

        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Error saving user"));

        boolean result = userService.createUser(userDTO);

        assertFalse(result);
    }

    @Test
    void testGetEmailsOfUsersWithDailyLearnReminder() {
        User user1 = new User(1L, "username1", "email1@example.com", "password", true, true, 20, "en");
        User user2 = new User(2L, "username2", "email2@example.com", "password", true, true, 20, "en");
        User user3 = new User(3L, "username3", "email3@example.com", "password", true, false, 30, "de"); // This user should be excluded
        User user4 = new User(4L, "username4", "email4@example.com", "password", false, true, 30, "de"); // This user should be excluded
        User user5 = new User(5L, "username5", "email5@example.com", "password", false, false, 30, "de"); // This user should be excluded

        userRepository.saveAll(List.of(user1, user2, user3, user4, user5));

        List<User> correctUserList = Arrays.asList(user1, user2);

        when(userRepository.findAllByIsVerifiedTrueAndGetsNotifiedTrue()).thenReturn(correctUserList);

        List<UserDailyReminderDTO> result = userService.getEmailsOfUsersWithDailyLearnReminder();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(UserDailyReminderDTO::username).containsExactly("username1", "username2");
        assertThat(result).extracting(UserDailyReminderDTO::email).containsExactly("email1@example.com", "email2@example.com");

        verify(userRepository, times(1)).findAllByIsVerifiedTrueAndGetsNotifiedTrue();
    }
}