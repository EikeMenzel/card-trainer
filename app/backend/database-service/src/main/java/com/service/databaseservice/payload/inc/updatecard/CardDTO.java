package com.service.databaseservice.payload.inc.updatecard;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardDTO {
    private final Long cardId;
    private final String question;
    private final Long imageId;

    public CardDTO(@JsonProperty("cardId") Long cardId, @JsonProperty("question") String question, @JsonProperty("imageId") Long imageId) {
        this.cardId = cardId;
        this.question = question;
        this.imageId = imageId;
    }

    public String getQuestion() {
        return question;
    }

    public Long getImageId() {
        return imageId;
    }

    public Long getCardId() {
        return cardId;
    }
}
