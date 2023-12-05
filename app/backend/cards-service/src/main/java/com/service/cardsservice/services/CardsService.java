package com.service.cardsservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.cardsservice.payload.out.cards.MultipleChoiceCardDTO;
import com.service.cardsservice.payload.out.cards.TextAnswerCardDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Service
public class CardsService {
    private final ObjectMapper objectMapper;
    private final DbQueryService dbQueryService;
    public CardsService(ObjectMapper objectMapper, DbQueryService dbQueryService) {
        this.objectMapper = objectMapper;
        this.dbQueryService = dbQueryService;
    }

    public HttpStatusCode saveCard(JsonNode cardNode, Long userId, Long deckId, MultipartFile[] images) {
        try {
            if (cardNode.has("textAnswer")) {
                var textCard = objectMapper.treeToValue(cardNode, TextAnswerCardDTO.class);
                return dbQueryService.saveCardByDeckIdAndUserId(userId, deckId, procressTextAnswerCardDTO(textCard, images));
            } else if (cardNode.has("choiceAnswers")) {
                var multipleChoiceCardDTO = objectMapper.treeToValue(cardNode, MultipleChoiceCardDTO.class);
                return dbQueryService.saveCardByDeckIdAndUserId(userId, deckId, processMultipleChoiceCardDTO(multipleChoiceCardDTO, images));
            } else {
                return HttpStatus.UNPROCESSABLE_ENTITY;
            }
        } catch (Exception e) {
            return HttpStatus.UNPROCESSABLE_ENTITY;
        }
    }

    public MultipleChoiceCardDTO processMultipleChoiceCardDTO(MultipleChoiceCardDTO multipleChoiceCardDTO, MultipartFile[] images) throws IOException {
        Optional<MultipartFile> optionalMultipartFile = findMultiPartFileByName(images, multipleChoiceCardDTO.getCardDTO().getImagePath());
        if(optionalMultipartFile.isPresent()) {
            multipleChoiceCardDTO.getCardDTO().setImageData(optionalMultipartFile.get().getBytes());
        }

        for (var choiceAnswer : multipleChoiceCardDTO.getChoiceAnswers()) {
            optionalMultipartFile = findMultiPartFileByName(images, choiceAnswer.getImagePath());
            if(optionalMultipartFile.isPresent())
                choiceAnswer.setImageData(optionalMultipartFile.get().getBytes());
        }
        return multipleChoiceCardDTO;
    }
    public TextAnswerCardDTO procressTextAnswerCardDTO(TextAnswerCardDTO textAnswerCardDTO, MultipartFile[] images) throws IOException {
        Optional<MultipartFile> optionalMultipartFile = findMultiPartFileByName(images, textAnswerCardDTO.getCardDTO().getImagePath());
        if(optionalMultipartFile.isPresent()) {
            textAnswerCardDTO.getCardDTO().setImageData(optionalMultipartFile.get().getBytes());
        }

        optionalMultipartFile = findMultiPartFileByName(images, textAnswerCardDTO.getImagePath());
        if(optionalMultipartFile.isPresent()) {
            textAnswerCardDTO.setImageData(optionalMultipartFile.get().getBytes());
        }

        return textAnswerCardDTO;
    }

    private Optional<MultipartFile> findMultiPartFileByName(MultipartFile[] images, String name) {
        if(images == null)
            return Optional.empty();

        return Arrays.stream(images).filter(multipartFile -> Objects.equals(multipartFile.getOriginalFilename(), name)).findFirst();
    }


}
