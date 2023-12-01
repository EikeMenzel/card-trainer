package com.service.cardsservice.payload.in.export;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class TextAnswerDTO extends CardExportDTO {
    @JsonProperty("imagePath")
    private String imagePath;

    @JsonProperty("textAnswer")
    private String textAnswer;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private byte[] image;

    public TextAnswerDTO(CardDTO cardDTO, String textAnswer, byte[] image) {
        super(cardDTO);
        this.textAnswer = textAnswer;
        this.image = image;
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public byte[] getImage() {
        return image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}