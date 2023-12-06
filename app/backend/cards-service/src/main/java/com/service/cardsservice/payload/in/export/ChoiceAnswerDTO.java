package com.service.cardsservice.payload.in.export;

import com.fasterxml.jackson.annotation.JsonProperty;
public final class ChoiceAnswerDTO {
    @JsonProperty("imagePath")
    private String imagePath;

    @JsonProperty("answer")
    private final String answer;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private byte[] image;
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