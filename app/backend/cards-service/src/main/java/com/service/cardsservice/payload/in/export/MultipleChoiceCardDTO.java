package com.service.cardsservice.payload.in.export;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class MultipleChoiceCardDTO extends CardExportDTO {
    @JsonProperty("choiceAnswers")
    private List<ChoiceAnswerDTO> choiceAnswers;

    public List<ChoiceAnswerDTO> getChoiceAnswers() {
        return choiceAnswers;
    }
}
