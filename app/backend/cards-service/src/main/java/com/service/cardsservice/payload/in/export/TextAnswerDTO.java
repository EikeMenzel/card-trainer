package com.service.cardsservice.payload.in.export;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.service.cardsservice.payload.Views;

public final class TextAnswerDTO extends CardExportDTO {
    @JsonView({Views.Database.class, Views.Export.class})
    @JsonProperty("textAnswer")
    private String textAnswer;
    @JsonView({Views.Database.class, Views.Export.class})
    @JsonProperty("imagePath")
    private String imagePath;

    @JsonView(Views.Database.class)
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