package com.service.mailservice.payload.inc;

import jakarta.validation.constraints.Email;
import org.springframework.lang.NonNull;

public class UserAccountInformationDTO {
    private String username;
    private String email;
    private Integer cardsToLearn;
    private Boolean receiveLearnNotification;
    private String langCode;

    public UserAccountInformationDTO(@NonNull String username, @NonNull @Email String email, @NonNull Integer cardsToLearn, @NonNull Boolean receiveLearnNotification, @NonNull String langCode) {
        this.username = username;
        this.email = email;
        this.cardsToLearn = cardsToLearn;
        this.receiveLearnNotification = receiveLearnNotification;
        this.langCode = langCode;
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
}