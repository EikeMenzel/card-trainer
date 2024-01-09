package com.service.databaseservice.controller;

import com.service.databaseservice.payload.inc.ImageDataDTO;
import com.service.databaseservice.repository.ImageRepository;
import com.service.databaseservice.services.ImageService;
import com.service.databaseservice.services.UserService;
import com.service.databaseservice.services.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/db")
public class ImageController {
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    public ImageController(ImageRepository imageRepository, ImageService imageService) {
        this.imageRepository = imageRepository;
        this.imageService = imageService;
    }

    @GetMapping("/users/{userId}/images/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long userId, @PathVariable Long imageId) {
        byte[] imageData = imageRepository.getImageByIdAndUserId(imageId, userId)
                .map(Utils::extractImageData)
                .orElse(null);

        if(imageData == null) {
            imageData = imageRepository.getImageById(imageId)
                    .stream()
                    .filter(image -> image.getUser() == null)
                    .findFirst()
                    .map(Utils::extractImageData)
                    .orElse(null);
        }

        return imageData == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(imageData);
    }

    @PostMapping("/users/{userId}/images")
    public ResponseEntity<Long> saveImage(@PathVariable Long userId, @RequestBody ImageDataDTO imageDataDTO) {
        return imageService.saveImage(userId, imageDataDTO.imageData())
                .map(id -> ResponseEntity.status(HttpStatus.CREATED).body(id))
                .orElse(ResponseEntity.internalServerError().build());
    }
}
