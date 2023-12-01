package com.service.databaseservice.payload.out.export;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
public final class MultipleChoiceCardDTO extends CardExportDTO {
    @JsonProperty("choiceAnswers")
    private List<ChoiceAnswerDTO> choiceAnswers;

    public MultipleChoiceCardDTO(CardDTO cardDTO, List<ChoiceAnswerDTO> choiceAnswers) {
        super(cardDTO, "multipleChoice");
        this.choiceAnswers = choiceAnswers;
    }
    public List<ChoiceAnswerDTO> getChoiceAnswers() {
        return choiceAnswers;
    }
}
