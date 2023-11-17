package com.service.databaseservice.services;

import com.service.databaseservice.model.User;
import com.service.databaseservice.payload.out.UserDTO;
import com.service.databaseservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

        Mockito.when(userRepository.getUserById(userId)).thenReturn(Optional.of(mockUser));

        Optional<String> userEmail = userService.getUserEmailById(userId);

        assertTrue(userEmail.isPresent());
        assertEquals("test@example.com", userEmail.get());
    }

    @Test
    void testGetUserEmailById_NonExistingUserId() {
        Long userId = 99L;

        Mockito.when(userRepository.getUserById(userId)).thenReturn(Optional.empty());

        Optional<String> userEmail = userService.getUserEmailById(userId);

        assertFalse(userEmail.isPresent());
    }

    @Test
    void testGetUserEmailById_InvalidUserId() {
        Long invalidUserId = -1L;

        Mockito.when(userRepository.getUserById(invalidUserId)).thenReturn(Optional.empty());

        Optional<String> userEmail = userService.getUserEmailById(invalidUserId);

        assertFalse(userEmail.isPresent());
    }

    @Test
    void testGetUserIdFromEmail_NonExistingUserEmail() {
        String nonExistingUserEmail = "nonexisting@example.com";

        Mockito.when(userRepository.getUserByEmail(nonExistingUserEmail)).thenReturn(Optional.empty());

        Optional<Long> userId = userService.getUserIdFromEmail(nonExistingUserEmail);

        assertFalse(userId.isPresent());
    }

    @Test
    void testGetUserIdFromEmail_InvalidUserEmail() {
        Mockito.when(userRepository.getUserByEmail(null)).thenReturn(Optional.empty());

        Optional<Long> userId = userService.getUserIdFromEmail(null);

        assertFalse(userId.isPresent());
    }

    @Test
    void testDoesEmailExist_ExistingEmail() {
        String existingEmail = "test@example.com";

        Mockito.when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        assertTrue(userService.doesEmailExist(existingEmail));
    }

    @Test
    void testDoesEmailExist_NonExistingEmail() {
        String nonExistingEmail = "nonexisting@example.com";

        Mockito.when(userRepository.existsByEmail(nonExistingEmail)).thenReturn(false);

        assertFalse(userService.doesEmailExist(nonExistingEmail));
    }
    @Test
    void testDoesEmailExist_InvalidEmail() {
        Mockito.when(userRepository.existsByEmail(null)).thenReturn(false);

        assertFalse(userService.doesEmailExist(null));
    }

    @Test
    void testIsUserVerifiedWhenUserExistsAndIsVerified() {
        Long userId = 1L;
        Mockito.when(userRepository.existsByIdAndIsVerifiedTrue(userId)).thenReturn(true);

        assertTrue(userService.isUserVerified(userId));
    }

    @Test
    void testIsUserVerifiedWhenUserDoesNotExist() {
        Long userId = 1L;
        Mockito.when(userRepository.existsByIdAndIsVerifiedTrue(userId)).thenReturn(false);

        assertFalse(userService.isUserVerified(userId));
    }

    @Test
    void testGetUserFromId_ValidUserId() {
        Long userId = 1L;
        User mockUser = new User("testUser", "test@example.com", "password");

        Mockito.when(userRepository.getUserById(userId)).thenReturn(Optional.of(mockUser));

        Optional<User> user = userService.getUserFromId(userId);

        assertTrue(user.isPresent());
        assertEquals("testUser", user.get().getUsername());
    }

    @Test
    void testGetUserFromId_NonExistingUserId() {
        Long nonExistingUserId = 99L;

        Mockito.when(userRepository.getUserById(nonExistingUserId)).thenReturn(Optional.empty());

        Optional<User> user = userService.getUserFromId(nonExistingUserId);

        assertFalse(user.isPresent());
    }

    @Test
    void testGetUserFromId_InvalidUserId() {
        Long invalidUserId = -1L;

        Mockito.when(userRepository.getUserById(invalidUserId)).thenReturn(Optional.empty());

        Optional<User> user = userService.getUserFromId(invalidUserId);

        assertFalse(user.isPresent());    }

    @Test
    void testCreateUser_ValidUserDTO() {
        UserDTO userDTO = new UserDTO("newUser", "newuser@example.com", "password");

        Mockito.when(userRepository.save(any(User.class))).thenReturn(new User(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword()));

        boolean result = userService.createUser(userDTO);

        assertTrue(result);
    }

    @Test
    void testCreateUser_ErrorInUserCreation() {
        UserDTO userDTO = new UserDTO("newUser", "newuser@example.com", "password");

        Mockito.when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Error saving user"));

        boolean result = userService.createUser(userDTO);

        assertFalse(result);
    }
}