package com.service.cardsservice.payload.out.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChoiceAnswerDTO {
    private final String answer;
    private final boolean isCorrect;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String imagePath;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private byte[] imageData;
    public ChoiceAnswerDTO(String answer, boolean isCorrect, byte[] imageData) {
        this.answer = answer;
        this.isCorrect = isCorrect;
        this.imageData = imageData;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isCorrect() {
        return isCorrect;
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
