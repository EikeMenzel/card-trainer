package com.service.databaseservice.payload.out.export;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextAnswerDTO.class, name = "textAnswer"),
        @JsonSubTypes.Type(value = MultipleChoiceCardDTO.class, name = "multipleChoice")
})
public class CardExportDTO {
    @JsonProperty("cardDTO")
    private CardDTO cardDTO;
    @JsonProperty("type")
    private String type;
    public CardExportDTO(CardDTO cardDTO, String type) {
        this.cardDTO = cardDTO;
        this.type = type;
    }

    public CardExportDTO() {
    }

    public CardDTO getCardDTO() {
        return cardDTO;
    }

    public String getType() {
        return type;
    }
}