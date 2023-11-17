package com.service.databaseservice.repository;

import com.service.databaseservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByIdAndIsVerifiedTrue(Long id);
}
