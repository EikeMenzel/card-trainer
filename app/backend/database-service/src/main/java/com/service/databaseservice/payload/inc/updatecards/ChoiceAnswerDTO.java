package com.service.databaseservice.payload.inc.updatecards;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChoiceAnswerDTO {
    private final Long choiceAnswerId;
    private final String answer;
    private final boolean isRightAnswer;
    private ImageDTO imageDTO;

    public ChoiceAnswerDTO(Long choiceAnswerId, String answer, boolean isRightAnswer, ImageDTO imageDTO) {
        this.choiceAnswerId = choiceAnswerId;
        this.answer = answer;
        this.isRightAnswer = isRightAnswer;
        this.imageDTO = imageDTO;
    }

    public String getAnswer() {
        return answer;
    }

    @JsonProperty("rightAnswer")
    public boolean getIsRightAnswer(){
        return isRightAnswer;
    }

    public ImageDTO getImageDTO() {
        return imageDTO;
    }

    public Long getChoiceAnswerId() {
        return choiceAnswerId;
    }

    public void setImageDTO(ImageDTO imageDTO) {
        this.imageDTO = imageDTO;
    }
}
