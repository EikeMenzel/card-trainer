package com.service.databaseservice.payload.savecard;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardDTO {
    private final String question;
    private final Long imageId;

    public CardDTO(@JsonProperty("question") String question, @JsonProperty("imageId") Long imageId) {
        this.question = question;
        this.imageId = imageId;
    }

    public String getQuestion() {
        return question;
    }

    public Long getImageId() {
        return imageId;
    }
}
