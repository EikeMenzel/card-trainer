package com.service.databaseservice.services;

import com.service.databaseservice.model.User;
import com.service.databaseservice.model.tutorial.TutorialPage;
import com.service.databaseservice.model.tutorial.TutorialType;
import com.service.databaseservice.model.tutorial.UserTutorialPage;
import com.service.databaseservice.repository.UserRepository;
import com.service.databaseservice.repository.tutorial.TutorialTypeRepository;
import com.service.databaseservice.repository.tutorial.UserTutorialPageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TutorialService {
    private final TutorialTypeRepository tutorialTypeRepository;
    private final UserTutorialPageRepository userTutorialPageRepository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(TutorialService.class);

    public TutorialService(TutorialTypeRepository tutorialTypeRepository, UserTutorialPageRepository userTutorialPageRepository, UserRepository userRepository) {
        this.tutorialTypeRepository = tutorialTypeRepository;
        this.userTutorialPageRepository = userTutorialPageRepository;
        this.userRepository = userRepository;
    }

    public boolean saveTutorialPage(TutorialPage tutorialPage, Long userId) {
        try {
            Optional<User> userOptional = userRepository.getUserById(userId);
            Optional<TutorialType> tutorialTypeOptional = tutorialTypeRepository.findTutorialTypeByType(tutorialPage.name());
            if(userOptional.isEmpty() || tutorialTypeOptional.isEmpty()) {
                return false;
            }

            userTutorialPageRepository.save(new UserTutorialPage(userOptional.get(), tutorialTypeOptional.get()));
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public boolean doesUserHasTutorialPage(TutorialPage tutorialPage, Long userId) {
        return userTutorialPageRepository.existsUserTutorialPageByUserIdAndTutorialTypeType(userId, tutorialPage.name());
    }
}
