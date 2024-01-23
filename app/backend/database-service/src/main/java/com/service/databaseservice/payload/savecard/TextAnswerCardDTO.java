package com.service.databaseservice.payload.savecard;

public class TextAnswerCardDTO{
    private CardDTO cardDTO;
    private String textAnswer;
    private Long imageId;

    public TextAnswerCardDTO(CardDTO cardDTO, String textAnswer, Long imageId) {
        this.cardDTO = cardDTO;
        this.imageId = imageId;
        this.textAnswer = textAnswer;
    }

    public TextAnswerCardDTO() {
    }

    public CardDTO getCardDTO() {
        return cardDTO;
    }

    public Long getImageId() {
        return imageId;
    }

    public String getTextAnswer() {
        return textAnswer;
    }
}
