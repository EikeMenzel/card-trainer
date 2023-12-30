package com.service.databaseservice.payload.import_function;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChoiceAnswerDTO {
    private String answer;
    private boolean isRightAnswer;
    private byte[] imageData;

    public ChoiceAnswerDTO(@JsonProperty("answer") String answer, boolean isRightAnswer, @JsonProperty("imageData") byte[] imageData) {
        this.answer = answer;
        this.isRightAnswer = isRightAnswer;
        this.imageData = imageData;
    }

    public ChoiceAnswerDTO() {
    }

    public String getAnswer() {
        return answer;
    }
    @JsonProperty("rightAnswer")
    public boolean getIsRightAnswer() {
        return isRightAnswer;
    }

    public byte[] getImageData() {
        return imageData;
    }
}
