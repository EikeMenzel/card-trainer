package com.service.cardsservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public final class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
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

    public static void deleteDirectory(File directory) throws IOException {
        if (directory.exists()) {
            try (Stream<Path> paths = Files.walk(directory.toPath())) {
                paths.sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                logger.error("Failed to delete path: " + path, e);
                            }
                        });
            }
        }
    }
}
