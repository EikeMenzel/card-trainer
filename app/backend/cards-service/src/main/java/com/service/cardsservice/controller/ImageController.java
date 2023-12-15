package com.service.cardsservice.controller;

import com.service.cardsservice.services.DbQueryService;
import com.service.cardsservice.services.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/images")
public class ImageController {
    private final DbQueryService dbQueryService;

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
}
