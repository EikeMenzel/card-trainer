package com.service.cardsservice.payload.in.export;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.service.cardsservice.payload.Views;

import java.util.List;

public final class MultipleChoiceCardDTO extends CardExportDTO {
    @JsonView({Views.Database.class, Views.Export.class})
    @JsonProperty("choiceAnswers")
    private List<ChoiceAnswerDTO> choiceAnswers;

    public List<ChoiceAnswerDTO> getChoiceAnswers() {
        return choiceAnswers;
    }
}
