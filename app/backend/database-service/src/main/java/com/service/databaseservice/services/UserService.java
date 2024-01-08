package com.service.databaseservice.services;

import com.service.databaseservice.model.User;
import com.service.databaseservice.payload.inc.UpdatePasswordDTO;
import com.service.databaseservice.payload.out.UserAccountInformationDTO;
import com.service.databaseservice.payload.out.UserDTO;
import com.service.databaseservice.payload.out.UserDailyReminderDTO;
import com.service.databaseservice.repository.UserRepository;
import com.service.databaseservice.repository.achievements.UserLoginTrackerRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserLoginTrackerRepository userLoginTrackerRepository;
    public UserService(UserRepository userRepository, UserLoginTrackerRepository userLoginTrackerRepository) {
        this.userRepository = userRepository;
        this.userLoginTrackerRepository = userLoginTrackerRepository;
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
        return user.map(value -> new UserDTO(value.getId(), value.getUsername(), value.getEmail(), value.getPassword(), value.getVerified()));
    }

    public Boolean isUserVerified(Long userId) {
        return userRepository.existsByIdAndIsVerifiedTrue(userId);
    }

    public List<UserDailyReminderDTO> getEmailsOfUsersWithDailyLearnReminder() {
        return userRepository.findAllByIsVerifiedTrueAndGetsNotifiedTrue()
                .stream()
                .map(user -> new UserDailyReminderDTO(user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean createUser(UserDTO userDTO) {
        try {
            userRepository.save(new User(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword(), userDTO.isVerify()));
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public Optional<UserAccountInformationDTO> getAccountInformation(Long id) {
        Optional<User> user = userRepository.getUserById(id);
        List<Date> loginDates = userLoginTrackerRepository.findDistinctLoginDatesByUserId(id);
        return user.map(value -> new UserAccountInformationDTO(value.getUsername(), value.getEmail(), value.getCardsPerSession(), value.getGetsNotified(), value.getLangCode(), calculateLoginStreak(loginDates)));
    }

    @Transactional
    public boolean updateAccountInformation(Long userId, UserAccountInformationDTO userAccountInformationDTO) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                return false;
            }
            var user = userOptional.get();

            if (!Objects.equals(user.getEmail(), userAccountInformationDTO.getEmail()) &&
                    userRepository.existsByEmailAndIdNot(userAccountInformationDTO.getEmail(), userId)) {
                return false;
            }

            user.setUsername(userAccountInformationDTO.getUsername());
            user.setEmail(userAccountInformationDTO.getEmail());
            user.setGetsNotified(userAccountInformationDTO.getReceiveLearnNotification());
            user.setCardsPerSession(userAccountInformationDTO.getCardsToLearn());
            user.setLangCode(userAccountInformationDTO.getLangCode());

            userRepository.save(user);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Transactional
    public boolean updateUserPassword(Long userId, String password) {
        try {
            Optional<User> userOptional = userRepository.getUserById(userId);
            if(userOptional.isEmpty())
                return false;

            var user = userOptional.get();
            if(password == null || password.equals(""))
                return false;

            user.setPassword(password);
            userRepository.save(user);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    private int calculateLoginStreak(List<Date> loginDates) {
        if(loginDates == null || loginDates.isEmpty())
            return 0;

        List<LocalDate> localDates = loginDates.stream().map(this::convertToLocalDateViaSqlDate).toList();

        var streak = 0;
        var today = LocalDate.now();

        for (LocalDate localDate : localDates) {
            if (localDate.equals(today)) {
                streak++;
                today = localDate.minusDays(1);
            }

            else if (localDate.isBefore(today)) {
                break;
            }
        }

        return streak;
    }

    public LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }
}
