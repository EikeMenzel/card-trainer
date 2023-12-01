package com.service.cardsservice.payload.in.export;

import com.fasterxml.jackson.annotation.JsonProperty;
public final class ChoiceAnswerDTO {
    @JsonProperty("imagePath")
    private String imagePath;

    @JsonProperty("answer")
    private final String answer;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final byte[] image;
    @JsonProperty("isCorrect")
    private final boolean isCorrect;

    public ChoiceAnswerDTO(
            @JsonProperty("answer") String answer,
            @JsonProperty("image") byte[] image,
            @JsonProperty("is_correct") boolean isCorrect
    ) {
        this.answer = answer;
        this.image = image;
        this.isCorrect = isCorrect;
    }

    @JsonProperty("answer")
    public String answer() {
        return answer;
    }

    @JsonProperty("image")
    public byte[] image() {
        return image;
    }

    @JsonProperty("is_correct")
    public boolean is_correct() {
        return isCorrect;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}