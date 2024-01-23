package com.service.cardsservice.payload.out.updatecards;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChoiceAnswerDTO {
    private Long choiceAnswerId;
    private String answer;
    private boolean isRightAnswer;
    private Long imageId;

    public ChoiceAnswerDTO(@JsonProperty("choiceAnswerId") Long choiceAnswerId, @JsonProperty("answer") String answer, @JsonProperty("rightAnswer") boolean isRightAnswer, @JsonProperty("imageId") Long imageId) {
        this.choiceAnswerId = choiceAnswerId;
        this.answer = answer;
        this.isRightAnswer = isRightAnswer;
        this.imageId = imageId;
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

    public Long getImageId() {
        return imageId;
    }

    public Long getChoiceAnswerId() {
        return choiceAnswerId;
    }
}
