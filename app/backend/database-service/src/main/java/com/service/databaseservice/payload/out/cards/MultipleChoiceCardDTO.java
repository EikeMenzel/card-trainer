package com.service.databaseservice.payload.out.cards;

import java.util.List;

public class MultipleChoiceCardDTO {
    private CardDTO cardDTO;
    private List<ChoiceAnswerDTO> choiceAnswers;

    public MultipleChoiceCardDTO(CardDTO cardDTO, List<ChoiceAnswerDTO> choiceAnswers) {
        this.cardDTO = cardDTO;
        this.choiceAnswers = choiceAnswers;
    }

    public CardDTO getCardDTO() {
        return cardDTO;
    }

    public List<ChoiceAnswerDTO> getChoiceAnswers() {
        return choiceAnswers;
    }
}
