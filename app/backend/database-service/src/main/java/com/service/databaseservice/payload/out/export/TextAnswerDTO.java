package com.service.databaseservice.payload.out.export;

import com.fasterxml.jackson.annotation.JsonProperty;
public final class TextAnswerDTO extends CardExportDTO {
    @JsonProperty("textAnswer")
    private String textAnswer;
    @JsonProperty("image")
    private byte[] image;

    public TextAnswerDTO(CardDTO cardDTO, String textAnswer, byte[] image) {
        super(cardDTO, "textAnswer");
        this.textAnswer = textAnswer;
        this.image = image;
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public byte[] getImage() {
        return image;
    }
}