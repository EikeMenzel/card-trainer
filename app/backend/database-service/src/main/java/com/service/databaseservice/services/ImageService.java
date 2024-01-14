package com.service.databaseservice.services;

import com.service.databaseservice.model.Image;
import com.service.databaseservice.model.User;
import com.service.databaseservice.repository.ImageRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.util.Optional;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final UserService userService;
    private final EntityManager entityManager;
    private final Logger logger = LoggerFactory.getLogger(ImageService.class);

    public ImageService(ImageRepository imageRepository, UserService userService, EntityManager entityManager) {
        this.imageRepository = imageRepository;
        this.userService = userService;
        this.entityManager = entityManager;
    }

    public Optional<Long> saveImage(Long userId, byte[] data) {
        try {
            Optional<User> userOptional = userService.getUserFromId(userId);
            return userOptional.flatMap(user -> imageRepository.save(new Image(generateBlob(data), user))
                    .getId()
                    .describeConstable());
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    private Blob generateBlob(byte[] data) {
        if (data == null)
            return null;

        try (var session = entityManager.unwrap(Session.class)) {
            return session.getLobHelper().createBlob(data);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }
}
