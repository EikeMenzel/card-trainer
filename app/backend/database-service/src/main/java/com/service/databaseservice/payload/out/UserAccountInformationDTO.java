package com.service.databaseservice.payload.out;

import jakarta.validation.constraints.Email;
import org.springframework.lang.NonNull;

public class UserAccountInformationDTO {
    private String username;
    private String email;
    private Integer cardsToLearn;
    private Boolean receiveLearnNotification;
    private String langCode;
    private Integer loginStreak;
    public UserAccountInformationDTO(@NonNull String username, @NonNull @Email String email, @NonNull Integer cardsToLearn, @NonNull Boolean receiveLearnNotification, @NonNull String langCode, @NonNull Integer loginStreak) {
        this.username = username;
        this.email = email;
        this.cardsToLearn = cardsToLearn;
        this.receiveLearnNotification = receiveLearnNotification;
        this.langCode = langCode;
        this.loginStreak = loginStreak;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Integer getCardsToLearn() {
        return cardsToLearn;
    }

    public Boolean getReceiveLearnNotification() {
        return receiveLearnNotification;
    }

    public String getLangCode() {
        return langCode;
    }

    public Integer getLoginStreak() {
        return loginStreak;
    }
}