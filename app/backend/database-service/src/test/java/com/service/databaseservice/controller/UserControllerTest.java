package com.service.databaseservice.controller;

import com.service.databaseservice.payload.out.UserDTO;
import com.service.databaseservice.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void testGetUserEmailFromId_UserFound() {
        Long userId = 1L;
        String userEmail = "test@example.com";

        when(userService.getUserEmailById(userId)).thenReturn(Optional.of(userEmail));

        ResponseEntity<String> response = userController.getUserEmailFromId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userEmail, response.getBody());
    }

    @Test
    void testGetUserEmailFromId_UserNotFound() {
        Long userId = 1L;

        when(userService.getUserEmailById(userId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = userController.getUserEmailFromId(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetIdFromUserEmail_UserFound() {
        String userEmail = "test@example.com";
        Long userId = 1L;

        when(userService.getUserIdFromEmail(userEmail)).thenReturn(Optional.of(userId));

        ResponseEntity<Long> response = userController.getIdFromUserEmail(userEmail);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userId, response.getBody());
    }

    @Test
    void testGetIdFromUserEmail_UserNotFound() {
        String userEmail = "test@example.com";

        when(userService.getUserIdFromEmail(userEmail)).thenReturn(Optional.empty());

        ResponseEntity<Long> response = userController.getIdFromUserEmail(userEmail);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDoesEmailExist_EmailExists() {
        String userEmail = "test@example.com";

        when(userService.doesEmailExist(userEmail)).thenReturn(true);

        ResponseEntity<Boolean> response = userController.doesEmailExist(userEmail);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void testDoesEmailExist_EmailDoesNotExist() {
        String userEmail = "nonexistent@example.com";

        when(userService.doesEmailExist(userEmail)).thenReturn(false);

        ResponseEntity<Boolean> response = userController.doesEmailExist(userEmail);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetIsUserVerifiedWhenUserIsVerified_True() {
        Long userId = 1L;
        when(userService.isUserVerified(userId)).thenReturn(true);

        ResponseEntity<Boolean> response = userController.getIsUserVerified(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void testGetIsUserVerifiedWhenUserIsVerified_False() {
        Long userId = 1L;
        when(userService.isUserVerified(userId)).thenReturn(Boolean.FALSE);

        ResponseEntity<Boolean> response = userController.getIsUserVerified(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateUser_Success() {
        UserDTO userDTO = new UserDTO("testUser", "test@example.com", "password");

        when(userService.createUser(userDTO)).thenReturn(true);

        ResponseEntity<?> response = userController.createUser(userDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testCreateUser_Failure() {
        UserDTO userDTO = new UserDTO("testUser", "test@example.com", "password");

        when(userService.createUser(userDTO)).thenReturn(false);

        ResponseEntity<?> response = userController.createUser(userDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetUserFromEmail_UserFound() {
        UserDTO mockUser = new UserDTO(1L, "username", "test@example.com", "password");
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(mockUser));

        ResponseEntity<UserDTO> response = userController.getUserFromEmail("test@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    void testGetUserFromEmail_UserNotFound() {
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.empty());

        ResponseEntity<UserDTO> response = userController.getUserFromEmail("nonexistent@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() == null || response.getBody().getId() == null);
    }
}
