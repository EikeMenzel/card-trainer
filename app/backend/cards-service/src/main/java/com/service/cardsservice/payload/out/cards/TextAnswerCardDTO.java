package com.service.cardsservice.payload.out.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TextAnswerCardDTO{
    private final CardDTO cardDTO;
    private final String textAnswer;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String imagePath;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private byte[] imageData;

    public TextAnswerCardDTO(CardDTO cardDTO, String textAnswer, byte[] imageData) {
        this.cardDTO = cardDTO;
        this.imageData = imageData;
        this.textAnswer = textAnswer;
    }

    public CardDTO getCardDTO() {
        return cardDTO;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
