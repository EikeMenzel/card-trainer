package com.service.cardsservice.controller;

import com.service.cardsservice.services.DbQueryService;
import com.service.cardsservice.services.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/images")
public class ImageController {
    private final DbQueryService dbQueryService;
    private final Logger logger = LoggerFactory.getLogger(ImageController.class);

    public ImageController(DbQueryService dbQueryService) {
        this.dbQueryService = dbQueryService;
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<?> getImage(@RequestHeader Long userId, @PathVariable Long imageId) {
        return dbQueryService.getImage(userId, imageId)
                .map(data -> Utils.getImageFormat(data)
                        .map(mediaType -> ResponseEntity.ok().contentType(mediaType).body(data))
                        .orElse(ResponseEntity.notFound().build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<?> saveImage(@RequestHeader Long userId, @RequestPart(value = "image") MultipartFile image) {
        try {
            if(Utils.getImageFormat(image.getBytes()).isEmpty())
                return ResponseEntity.unprocessableEntity().build();

            return dbQueryService.saveImage(userId, image.getBytes())
                    .map(id -> ResponseEntity.status(HttpStatus.CREATED).body(id)).orElse(ResponseEntity.internalServerError().build());
        } catch (IOException e) {
            logger.debug(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
