package com.service.databaseservice.payload.out.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardDTO {
    private final String question;
    private final byte[] imageData;

    public CardDTO(@JsonProperty("question") String question, @JsonProperty("imageData") byte[] imageData) {
        this.question = question;
        this.imageData = imageData;
    }

    public String getQuestion() {
        return question;
    }

    public byte[] getImageData() {
        return imageData;
    }
}
