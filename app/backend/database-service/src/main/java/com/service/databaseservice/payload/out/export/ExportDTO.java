package com.service.databaseservice.payload.out.export;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
public record ExportDTO(
        @JsonProperty("deckName") String deckName,
        @JsonProperty("cardExportDTOList") List<CardExportDTO> cardExportDTOList
) { }
