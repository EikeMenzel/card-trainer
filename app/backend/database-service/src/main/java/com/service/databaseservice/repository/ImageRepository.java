package com.service.databaseservice.repository;

import com.service.databaseservice.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> getImageByIdAndUserId(Long imageId, Long userId);
    Optional<Image> getImageById(Long imageId);
}
