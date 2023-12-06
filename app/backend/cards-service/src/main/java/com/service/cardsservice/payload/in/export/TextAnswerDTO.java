package com.service.cardsservice.payload.in.export;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class TextAnswerDTO extends CardExportDTO {
    @JsonProperty("textAnswer")
    private String textAnswer;
    @JsonProperty("imagePath")
    private String imagePath;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private byte[] image;

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

    public void setImage(byte[] image) {
        this.image = image;
    }
}