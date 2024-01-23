package com.service.databaseservice.services;

import com.service.databaseservice.model.Image;
import com.service.databaseservice.model.User;
import com.service.databaseservice.repository.ImageRepository;
import com.service.databaseservice.services.UserService;
import jakarta.persistence.EntityManager;
import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.LobCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Blob;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UserService userService;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // this test does not work, you can't access the LobCreator properly

    /*@Test
    void saveImage_WithValidUserAndData() {
        Long userId = 1L;
        User user = new User();
        byte[] data = new byte[]{1, 2, 3};
        Image savedImage = new Image();

        when(userService.getUserFromId(userId)).thenReturn(Optional.of(user));
        when(imageRepository.save(any(Image.class))).thenReturn(savedImage);

        Session mockSession = mock(Session.class);
        LobCreator mockLobCreator = mock(LobCreator.class);
        Blob mockBlob = mock(Blob.class);
        when(entityManager.unwrap(Session.class)).thenReturn(mockSession);
        when(mockSession.getLobHelper()).thenReturn(mockSession.getLobHelper());
        when(mockLobCreator.createBlob(data)).thenReturn(mockBlob); // Mock Blob creation

        Optional<Long> result = imageService.saveImage(userId, data);

        assertTrue(result.isPresent());
        verify(imageRepository).save(any(Image.class));
    }*/



    @Test
    void saveImage_WithNonExistingUser() {
        Long userId = 1L;
        byte[] data = new byte[]{1, 2, 3};

        when(userService.getUserFromId(userId)).thenReturn(Optional.empty());

        Optional<Long> result = imageService.saveImage(userId, data);

        assertFalse(result.isPresent());
        verify(imageRepository, never()).save(any(Image.class));
    }
}

