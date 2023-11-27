package com.service.databaseservice.services;

import com.service.databaseservice.model.User;
import com.service.databaseservice.payload.inc.UpdatePasswordDTO;
import com.service.databaseservice.payload.out.UserAccountInformationDTO;
import com.service.databaseservice.payload.out.UserDTO;
import com.service.databaseservice.payload.out.UserDailyReminderDTO;
import com.service.databaseservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private final UserRepository userRepository;

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
        return user.map(value -> new UserAccountInformationDTO(value.getUsername(), value.getEmail(), value.getCardsPerSession(), value.getGetsNotified(), value.getLangCode()));
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
            return false;
        }
        return true;
    }

    @Transactional
    public boolean updateUserPassword(Long userId, UpdatePasswordDTO updatePasswordDTO) {
        try {
            Optional<User> userOptional = userRepository.getUserById(userId);
            if(userOptional.isEmpty())
                return false;

            var user = userOptional.get();
            if(updatePasswordDTO.password() == null || updatePasswordDTO.password().equals(""))
                return false;

            user.setPassword(updatePasswordDTO.password());
            userRepository.save(user);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
