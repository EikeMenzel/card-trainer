package com.service.databaseservice.payload.out.getcarddetails;

import java.util.List;

public record MultipleChoiceCardDTO(CardDTO cardDTO, Long multipleChoiceCardId, List<ChoiceAnswerDTO> choiceAnswers) {
}
