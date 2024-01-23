package com.service.cardsservice.payload.in.export;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.service.cardsservice.payload.Views;

public final class ChoiceAnswerDTO {
    @JsonView({Views.Database.class, Views.Export.class})
    @JsonProperty("imagePath")
    private String imagePath;

    @JsonView({Views.Database.class, Views.Export.class})
    @JsonProperty("answer")
    private final String answer;
    @JsonView(Views.Database.class)
    private byte[] image;
    @JsonView({Views.Database.class, Views.Export.class})
    @JsonProperty("isRightAnswer")
    private final boolean isRightAnswer;

    public ChoiceAnswerDTO(
            @JsonProperty("answer") String answer,
            @JsonProperty("image") byte[] image,
            @JsonProperty("isRightAnswer") boolean isRightAnswer
    ) {
        this.answer = answer;
        this.image = image;
        this.isRightAnswer = isRightAnswer;
    }

    @JsonProperty("answer")
    public String answer() {
        return answer;
    }

    @JsonProperty("image")
    public byte[] image() {
        return image;
    }

    @JsonProperty("isRightAnswer")
    public boolean isRightAnswer() {
        return isRightAnswer;
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