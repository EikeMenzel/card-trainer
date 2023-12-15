package com.service.cardsservice.services;

import org.springframework.http.MediaType;

import java.util.Optional;

public final class Utils {
    private Utils() { }
    public static Optional<MediaType> getImageFormat(byte[] imageData) {
        if (imageData.length >= 2) {
            if (imageData[0] == (byte) 0xFF && imageData[1] == (byte) 0xD8) {
                return Optional.of(MediaType.IMAGE_JPEG);
            } else if (imageData[0] == (byte) 0x89 && imageData[1] == (byte) 0x50) {
                return Optional.of(MediaType.IMAGE_PNG);
            }
        }
        return Optional.empty();
    }
}
