package com.service.databaseservice.services;

import com.service.databaseservice.model.User;
import com.service.databaseservice.payload.out.UserDTO;
import com.service.databaseservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final Logger logger =  LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<String> getUserEmailById(Long id) {
        var user = userRepository.getUserById(id);
        return user.map(User::getEmail);
    }

    public Optional<Long> getUserIdFromEmail(String email) {
        var user = userRepository.getUserByEmail(email);
        return user.map(User::getId);
    }

    public Boolean doesEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> getUserFromId(Long id) {
        return userRepository.getUserById(id);
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        Optional<User> user = userRepository.getUserByEmail(email);
        return user.map(value -> new UserDTO(value.getId(), value.getUsername(), value.getEmail(), value.getPassword()));
    }

    public Boolean isUserVerified(Long userId) {
        return userRepository.existsByIdAndIsVerifiedTrue(userId);
    }

    public boolean createUser(UserDTO userDTO) {
        try {
            userRepository.save(new User(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword()));
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
