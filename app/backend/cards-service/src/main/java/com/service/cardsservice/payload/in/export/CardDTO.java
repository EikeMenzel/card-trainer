package com.service.cardsservice.payload.in.export;

import com.fasterxml.jackson.annotation.JsonProperty;
public final class CardDTO {
    @JsonProperty("question")
    private String question;
    @JsonProperty("imagePath")
    private String imagePath;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private byte[] image;
    @JsonProperty("cardTypeId")
    private Long cardTypeId;

    public CardDTO(String question, byte[] image, Long cardTypeId) {
        this.question = question;
        this.image = image;
        this.cardTypeId = cardTypeId;
    }

    public CardDTO() {
    }

    public String question() {
        return question;
    }

    public byte[] image() {
        return image;
    }

    public Long cardTypeId() {
        return cardTypeId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}