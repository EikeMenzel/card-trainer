package com.service.databaseservice.controller;

import com.service.databaseservice.payload.inc.ImageDataDTO;
import com.service.databaseservice.payload.out.export.ExportDTO;
import com.service.databaseservice.repository.ImageRepository;
import com.service.databaseservice.services.ImageService;
import com.service.databaseservice.services.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Retrieve Image",
            description = "Fetches an image by its ID for the specified user. If the image is not found under the user, it tries to fetch a public image with the same ID.<br><br>" +
                    "<strong>Note:</strong> Requires valid user ID and image ID." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Image successfully retrieved", content = {
                            @Content(mediaType = "image/jpeg"),
                            @Content(mediaType = "image/png")
                    }),
                    @ApiResponse(responseCode = "404", description = "Image not found"),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    @GetMapping("/users/{userId}/images/{imageId}")
    public ResponseEntity<byte[]> getImage(
            @Parameter(description = "User ID of the requester", required = true) @PathVariable Long userId,
            @Parameter(description = "ID of the image to retrieve", required = true) @PathVariable Long imageId) {
        byte[] imageData = imageRepository.getImageByIdAndUserId(imageId, userId)
                .map(Utils::extractImageData)
                .orElse(null);

        if (imageData == null) {
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
    @Operation(summary = "Save Image",
            description = "Saves an image for the specified user.<br><br>" +
                    "<strong>Note:</strong> Requires valid user ID and image data." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Image successfully saved", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Long> saveImage(
            @Parameter(description = "User ID who is saving the image", required = true) @PathVariable Long userId,
            @Parameter(description = "ImageDataDTO contains the image data as a byte[]", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageDataDTO.class))) @RequestBody ImageDataDTO imageDataDTO) {
        return imageService.saveImage(userId, imageDataDTO.imageData())
                .map(id -> ResponseEntity.status(HttpStatus.CREATED).body(id))
                .orElse(ResponseEntity.internalServerError().build());
    }
}
