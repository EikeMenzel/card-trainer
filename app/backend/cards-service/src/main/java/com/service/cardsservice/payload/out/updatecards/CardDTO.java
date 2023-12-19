package com.service.cardsservice.payload.out.updatecards;

public class CardDTO {
    private final Long cardId;
    private final String question;
    private ImageDTO imageDTO;

    public CardDTO(Long cardId, String question, ImageDTO imageDTO) {
        this.cardId = cardId;
        this.question = question;
        this.imageDTO = imageDTO;
    }

    public Long getCardId() {
        return cardId;
    }

    public String getQuestion() {
        return question;
    }

    public ImageDTO getImageDTO() {
        return imageDTO;
    }

    public void setImageDTO(ImageDTO imageDTO) {
        this.imageDTO = imageDTO;
    }
}
