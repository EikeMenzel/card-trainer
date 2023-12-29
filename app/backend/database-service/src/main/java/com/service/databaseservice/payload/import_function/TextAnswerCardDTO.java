package com.service.databaseservice.payload.import_function;

public class TextAnswerCardDTO{
    private final CardDTO cardDTO;
    private final String textAnswer;
    private final byte[] imageData;

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
}
