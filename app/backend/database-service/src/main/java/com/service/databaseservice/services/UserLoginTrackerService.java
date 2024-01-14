package com.service.databaseservice.services;

import com.service.databaseservice.model.User;
import com.service.databaseservice.model.achievements.UserLoginTracker;
import com.service.databaseservice.repository.UserRepository;
import com.service.databaseservice.repository.achievements.UserLoginTrackerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserLoginTrackerService {
    private final Logger logger = LoggerFactory.getLogger(UserLoginTrackerService.class);
    private final UserLoginTrackerRepository userLoginTrackerRepository;
    private final UserRepository userRepository;

    public UserLoginTrackerService(UserLoginTrackerRepository userLoginTrackerRepository, UserRepository userRepository) {
        this.userLoginTrackerRepository = userLoginTrackerRepository;
        this.userRepository = userRepository;
    }

    public HttpStatusCode saveUserLogin(Long userId) {
        try {
            if(wasUserLoggedInToday(userId)) //there was already an entry created today
                return HttpStatus.CONFLICT;

            Optional<User> userOptional = userRepository.getUserById(userId);
            if(userOptional.isEmpty())
                return HttpStatus.NOT_FOUND;

            userLoginTrackerRepository.save(new UserLoginTracker(userOptional.get()));
            return HttpStatus.CREATED;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
    
    public boolean wasUserLoggedInToday(Long userId) {
        return userLoginTrackerRepository.existsByUserId(userId);
    }
}
