package com.service.cardsservice.payload.in.export;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.service.cardsservice.payload.Views;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextAnswerDTO.class, name = "textAnswer"),
        @JsonSubTypes.Type(value = MultipleChoiceCardDTO.class, name = "multipleChoice")
})
public class CardExportDTO {
    @JsonView({Views.Database.class, Views.Export.class})
    @JsonProperty("cardDTO")
    private CardDTO cardDTO;
    public CardExportDTO(CardDTO cardDTO) {
        this.cardDTO = cardDTO;
    }

    public CardExportDTO() {
    }

    public CardDTO getCardDTO() {
        return cardDTO;
    }
}