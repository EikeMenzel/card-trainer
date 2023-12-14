package com.service.cardsservice.payload.out.savecards;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChoiceAnswerDTO {
    private final String answer;
    private final boolean isRightAnswer;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String imagePath;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private byte[] imageData;
    public ChoiceAnswerDTO(String answer, boolean isRightAnswer, byte[] imageData) {
        this.answer = answer;
        this.isRightAnswer = isRightAnswer;
        this.imageData = imageData;
    }

    public String getAnswer() {
        return answer;
    }

    @JsonProperty("rightAnswer")
    public boolean getIsRightAnswer(){
        return isRightAnswer;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
