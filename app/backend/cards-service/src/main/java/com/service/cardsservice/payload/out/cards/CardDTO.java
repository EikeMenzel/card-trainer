package com.service.cardsservice.payload.out.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardDTO {
    private final String question;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String imagePath;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private byte[] imageData;

    public CardDTO(String question, byte[] imageData) {
        this.question = question;
        this.imageData = imageData;
    }

    public String getQuestion() {
        return question;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
