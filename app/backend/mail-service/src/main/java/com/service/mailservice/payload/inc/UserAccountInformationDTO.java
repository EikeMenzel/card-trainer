package com.service.mailservice.payload.inc;

import jakarta.validation.constraints.Email;
import org.springframework.lang.NonNull;

public record UserAccountInformationDTO(String username, String email, Integer cardsToLearn,
                                        Boolean receiveLearnNotification, String langCode) {
    public UserAccountInformationDTO(@NonNull String username, @NonNull @Email String email, @NonNull Integer cardsToLearn, @NonNull Boolean receiveLearnNotification, @NonNull String langCode) {
        this.username = username;
        this.email = email;
        this.cardsToLearn = cardsToLearn;
        this.receiveLearnNotification = receiveLearnNotification;
        this.langCode = langCode;
    }
}