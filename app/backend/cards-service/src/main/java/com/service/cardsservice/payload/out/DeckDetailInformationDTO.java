package com.service.cardsservice.payload.out;

import java.sql.Timestamp;
import java.util.List;

public class DeckDetailInformationDTO extends DeckInformationDTO {
    private List<Integer> deckLearnState;
    public DeckDetailInformationDTO(Long deckId, String deckName, Integer deckSize, Integer cardsToLearn, Timestamp lastLearned, List<Integer> deckLearnState) {
        super(deckId, deckName, deckSize, cardsToLearn, lastLearned);
        this.deckLearnState = deckLearnState;
    }
    public DeckDetailInformationDTO() {
    }

    public List<Integer> getDeckLearnState() {
        return deckLearnState;
    }
}
