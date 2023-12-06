package com.service.databaseservice.payload.out.export;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class ChoiceAnswerDTO {
    @JsonProperty("answer")
    private final String answer;
    @JsonProperty("image")
    private final byte[] image;
    @JsonProperty("isRightAnswer")
    private boolean isRightAnswer;

    public ChoiceAnswerDTO(
            @JsonProperty("answer") String answer,
            @JsonProperty("image") byte[] image,
            @JsonProperty("rightAnswer") boolean isRightAnswer
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

    @JsonProperty("rightAnswer")
    public boolean getIsRightAnswer() {
        return isRightAnswer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ChoiceAnswerDTO) obj;
        return Objects.equals(this.answer, that.answer) &&
                Objects.equals(this.image, that.image) &&
                this.isRightAnswer == that.isRightAnswer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(answer, image, isRightAnswer);
    }

    @Override
    public String toString() {
        return "ChoiceAnswerDTO[" +
                "answer=" + answer + ", " +
                "image=" + image + ", " +
                "isRightAnswer=" + isRightAnswer + ']';
    }
}