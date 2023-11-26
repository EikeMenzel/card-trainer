package com.service.cardsservice.payload.out;

import java.sql.Timestamp;

public class DeckInformationDTO {
    private Long deckId;
    private String deckName;
    private Integer deckSize;
    private Integer cardsToLearn;
    private Timestamp lastLearned;

    public DeckInformationDTO(Long deckId, String deckName, Integer deckSize, Integer cardsToLearn, Timestamp lastLearned) {
        this.deckId = deckId;
        this.deckName = deckName;
        this.deckSize = deckSize;
        this.cardsToLearn = cardsToLearn;
        this.lastLearned = lastLearned;
    }

    public DeckInformationDTO() {
    }

    public Long getDeckId() {
        return deckId;
    }

    public String getDeckName() {
        return deckName;
    }

    public Integer getDeckSize() {
        return deckSize;
    }

    public Integer getCardsToLearn() {
        return cardsToLearn;
    }

    public Timestamp getLastLearned() {
        return lastLearned;
    }
}
