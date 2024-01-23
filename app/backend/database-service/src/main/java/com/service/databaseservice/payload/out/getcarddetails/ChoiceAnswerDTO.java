package com.service.databaseservice.payload.out.getcarddetails;

public record ChoiceAnswerDTO(Long choiceAnswerId, String answer, boolean rightAnswer, Long imageId) {
}
