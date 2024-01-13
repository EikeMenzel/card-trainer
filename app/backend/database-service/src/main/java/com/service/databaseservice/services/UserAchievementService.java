package com.service.databaseservice.services;

import com.service.databaseservice.model.User;
import com.service.databaseservice.model.achievements.Achievement;
import com.service.databaseservice.model.achievements.UserAchievement;
import com.service.databaseservice.payload.out.AchievementDetailsDTO;
import com.service.databaseservice.repository.UserRepository;
import com.service.databaseservice.repository.achievements.AchievementRepository;
import com.service.databaseservice.repository.achievements.UserAchievementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserAchievementService {
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserAchievementService.class);
    public UserAchievementService(UserAchievementRepository userAchievementRepository, AchievementRepository achievementRepository, UserRepository userRepository) {
        this.userAchievementRepository = userAchievementRepository;
        this.achievementRepository = achievementRepository;
        this.userRepository = userRepository;
    }

    public List<Long> getAllUserAchievementIdsByUserId(Long userId) {
        return userAchievementRepository.getUserAchievementsByUser_Id(userId)
                .stream()
                .filter(entry -> !entry.getAchievement().getDaily())
                .map(entry -> entry.getAchievement().getId())
                .collect(Collectors.toList());
    }
   // @Transactional
    public boolean saveUserAchievement(Long userId, Long achievementId) {
        try {
            Optional<User> userOptional = userRepository.getUserById(userId);
            Optional<Achievement> achievementOptional = achievementRepository.findById(achievementId);

            if(userOptional.isEmpty() || achievementOptional.isEmpty())
                return false;

            userAchievementRepository.save(new UserAchievement(userOptional.get(), achievementOptional.get()));
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public boolean doesUserHaveAchievement(Long userId, Long achievementId) {
        return userAchievementRepository.hasUserAchieved(achievementId, userId);
    }

    public Optional<AchievementDetailsDTO> getAchievementDetails(Long achievementId) {
        return achievementRepository.findById(achievementId)
                .map(achievement -> new AchievementDetailsDTO(achievement.getId(), achievement.getName(), achievement.getDescription(), achievement.getDaily(), achievement.getImageData().getId()));
    }
}
