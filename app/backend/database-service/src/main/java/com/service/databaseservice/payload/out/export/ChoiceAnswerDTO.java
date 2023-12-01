package com.service.databaseservice.payload.out.export;

import com.fasterxml.jackson.annotation.JsonProperty;
public record ChoiceAnswerDTO(
        @JsonProperty("answer") String answer,
        @JsonProperty("image") byte[] image,
        @JsonProperty("isCorrect") boolean is_correct
) { }