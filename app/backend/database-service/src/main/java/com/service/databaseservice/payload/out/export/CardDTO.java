package com.service.databaseservice.payload.out.export;

import com.fasterxml.jackson.annotation.JsonProperty;
public record CardDTO(@JsonProperty("question") String question, @JsonProperty("image") byte[] image,
                      @JsonProperty("cardTypeId") Long cardTypeId) {
    public CardDTO(String question, byte[] image, Long cardTypeId) {
        this.question = question;
        this.image = image;
        this.cardTypeId = cardTypeId;
    }

    @Override
    public String question() {
        return question;
    }

    @Override
    public byte[] image() {
        return image;
    }

    @Override
    public Long cardTypeId() {
        return cardTypeId;
    }
}