package com.service.databaseservice.payload.out.cards;

public class CardDTO {
    private final String question;
    private final byte[] imageData;

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
}
