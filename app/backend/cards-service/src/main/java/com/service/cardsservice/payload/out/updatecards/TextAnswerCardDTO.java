package com.service.cardsservice.payload.out.updatecards;

public class TextAnswerCardDTO {
    private final Long textAnswerCardId;
    private final CardDTO cardDTO;
    private final String textAnswer;
    private ImageDTO imageDTO;

    public TextAnswerCardDTO(Long textAnswerCardId, CardDTO cardDTO, String textAnswer, ImageDTO imageDTO) {
        this.textAnswerCardId = textAnswerCardId;
        this.cardDTO = cardDTO;
        this.imageDTO = imageDTO;
        this.textAnswer = textAnswer;
    }

    public CardDTO getCardDTO() {
        return cardDTO;
    }
    public String getTextAnswer() {
        return textAnswer;
    }

    public ImageDTO getImageDTO() {
        return imageDTO;
    }

    public Long getTextAnswerCardId() {
        return textAnswerCardId;
    }

    public void setImageDTO(ImageDTO imageDTO) {
        this.imageDTO = imageDTO;
    }
}
