package com.service.cardsservice.payload.out.savecards;

import java.util.List;

public class MultipleChoiceCardDTO {
    private final CardDTO cardDTO;
    private final List<ChoiceAnswerDTO> choiceAnswers;

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
