package com.service.userservice.payload.inc;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;

public class UserAccountInformationDTO {
    @Size(min = 4, max = 30) private final String username;
    @Email @Size(min = 6, max = 64) private final String email;
    @Min(value = 0) private final Integer cardsToLearn;
    private final Boolean receiveLearnNotification;
    @Size(min = 2, max = 3) private final String langCode;

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