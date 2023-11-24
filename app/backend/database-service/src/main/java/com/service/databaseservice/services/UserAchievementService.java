package com.service.databaseservice.services;

import com.service.databaseservice.model.achievements.UserAchievement;
import com.service.databaseservice.repository.achievements.UserAchievementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAchievementService {
    private final UserAchievementRepository userAchievementRepository;

    public UserAchievementService(UserAchievementRepository userAchievementRepository) {
        this.userAchievementRepository = userAchievementRepository;
    }

    public List<Long> getAllUserAchievementIdsByUserId(Long userId) {
        return userAchievementRepository.getUserAchievementByUser_Id(userId)
                .stream().map(entry -> entry.getAchievement().getId())
                .collect(Collectors.toList());
    }
}
