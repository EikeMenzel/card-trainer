package com.service.databaseservice.payload.out.getcarddetails;

public record TextAnswerCardDTO(CardDTO cardDTO, Long textAnswerCardId, String textAnswer, Long imageId) {
}
