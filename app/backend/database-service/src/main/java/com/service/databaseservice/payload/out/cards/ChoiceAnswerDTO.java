package com.service.databaseservice.payload.out.cards;

public class ChoiceAnswerDTO {
    private final String answer;
    private final boolean isCorrect;
    private final byte[] imageData;

    public ChoiceAnswerDTO(String answer, boolean isCorrect, byte[] imageData) {
        this.answer = answer;
        this.isCorrect = isCorrect;
        this.imageData = imageData;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public byte[] getImageData() {
        return imageData;
    }
}
