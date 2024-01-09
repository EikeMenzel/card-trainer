package com.service.cardsservice.controller;

import com.service.cardsservice.services.DbQueryService;
import com.service.cardsservice.services.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("api/v1/images")
public class ImageController {
    private final DbQueryService dbQueryService;
    private final Logger logger = LoggerFactory.getLogger(ImageController.class);

    public ImageController(DbQueryService dbQueryService) {
        this.dbQueryService = dbQueryService;
    }

    @GetMapping("/{imageId}")
    @Operation(summary = "Retrieve Image",
            description = "Fetches an image by its ID for the specified user.<br><br>" +
                    "<strong>Note:</strong> Ensure the image ID is valid and the user has access to the image." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Image successfully retrieved",
                            content = {
                                    @Content(mediaType = "image/jpeg"),
                                    @Content(mediaType = "image/png")
                            }),
                    @ApiResponse(responseCode = "404", description = "Image not found"),
                    @ApiResponse(responseCode = "500", description = "Server could not be reached")
            })
    public ResponseEntity<byte[]> getImage(
            @Parameter(description = "User ID of the requester", required = true) @RequestHeader Long userId,
            @Parameter(description = "ID of the image to retrieve", required = true) @PathVariable Long imageId) {
        return dbQueryService.getImage(userId, imageId)
                .map(data -> Utils.getImageFormat(data)
                        .map(mediaType -> ResponseEntity.ok().contentType(mediaType).body(data))
                        .orElse(ResponseEntity.notFound().build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    @Operation(summary = "Save Image",
            description = "Uploads and saves an image to the user's account.<br><br>" +
                    "<strong>Note:</strong> The image file must be in a supported format." +
                    "<br><br><strong>⚠️ Warning:</strong> This route will not work, if you aren't logged in!",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Image successfully created and stored"),
                    @ApiResponse(responseCode = "422", description = "Unprocessable entity, invalid image format"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Long> saveImage(
            @Parameter(description = "User ID of the uploader", required = true) @RequestHeader Long userId,
            @Parameter(description = "Image file to upload", required = true, content = @Content(mediaType = "multipart/form-data")) @RequestPart(value = "image") MultipartFile image) {
        try {
            if (Utils.getImageFormat(image.getBytes()).isEmpty())
                return ResponseEntity.unprocessableEntity().build();

            return dbQueryService.saveImage(userId, image.getBytes())
                    .map(id -> ResponseEntity.status(HttpStatus.CREATED).body(id)).orElse(ResponseEntity.internalServerError().build());
        } catch (IOException e) {
            logger.debug(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
