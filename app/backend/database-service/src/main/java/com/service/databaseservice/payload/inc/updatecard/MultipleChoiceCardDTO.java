package com.service.databaseservice.payload.inc.updatecard;

import java.util.List;

public class MultipleChoiceCardDTO {
    private final Long multipleChoiceCardId;
    private final CardDTO cardDTO;
    private final List<ChoiceAnswerDTO> choiceAnswers;

    public MultipleChoiceCardDTO(Long multipleChoiceCardId, CardDTO cardDTO, List<ChoiceAnswerDTO> choiceAnswers) {
        this.multipleChoiceCardId = multipleChoiceCardId;
        this.cardDTO = cardDTO;
        this.choiceAnswers = choiceAnswers;
    }

    public CardDTO getCardDTO() {
        return cardDTO;
    }

    public List<ChoiceAnswerDTO> getChoiceAnswers() {
        return choiceAnswers;
    }

    public Long getMultipleChoiceCardId() {
        return multipleChoiceCardId;
    }
}
