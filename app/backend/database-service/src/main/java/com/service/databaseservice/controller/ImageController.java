package com.service.databaseservice.controller;

import com.service.databaseservice.repository.ImageRepository;
import com.service.databaseservice.services.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/db")
public class ImageController {
    private final ImageRepository imageRepository;

    public ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping("/users/{userId}/images/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long userId, @PathVariable Long imageId) {
        byte[] imageData = imageRepository.getImageByIdAndUserId(imageId, userId)
                .map(Utils::extractImageData)
                .orElse(null);

        return imageData == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(imageData);
    }
}
