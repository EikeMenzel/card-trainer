package com.service.cardsservice.payload.in.export;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.service.cardsservice.payload.Views;

public final class CardDTO {
    @JsonView({Views.Database.class, Views.Export.class})
    @JsonProperty("question")
    private String question;

    @JsonView({Views.Database.class, Views.Export.class})
    @JsonProperty("imagePath")
    private String imagePath;
    @JsonView({Views.Database.class})
    private byte[] image;
    @JsonView({Views.Database.class, Views.Export.class})
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