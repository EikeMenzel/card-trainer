package com.service.cardsservice.payload.out.updatecards;

public class TextAnswerCardDTO {
    private final Long textAnswerCardId;
    private final CardDTO cardDTO;
    private final String textAnswer;
    private final Long imageId;

    public TextAnswerCardDTO(Long textAnswerCardId, CardDTO cardDTO, String textAnswer, Long imageId) {
        this.textAnswerCardId = textAnswerCardId;
        this.cardDTO = cardDTO;
        this.imageId = imageId;
        this.textAnswer = textAnswer;
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

    public Long getTextAnswerCardId() {
        return textAnswerCardId;
    }
}
