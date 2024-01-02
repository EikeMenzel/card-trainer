package com.service.cardsservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(CardsService.class);

    public CardsService(ObjectMapper objectMapper, DbQueryService dbQueryService) {
        this.objectMapper = objectMapper;
        this.dbQueryService = dbQueryService;
    }

    //saveCard
    public HttpStatusCode saveCard(JsonNode cardNode, Long userId, Long deckId) {
        try {
            if (cardNode.has("textAnswer") || cardNode.has("choiceAnswers")) {
                return dbQueryService.saveCardByDeckIdAndUserId(userId, deckId, cardNode);
            } else {
                return HttpStatus.UNPROCESSABLE_ENTITY;
            }
        } catch (Exception e) {
            return HttpStatus.UNPROCESSABLE_ENTITY;
        }
    }

    //updateCard
    public HttpStatusCode updateCard(JsonNode cardNode, Long userId, Long deckId, Long cardId) {
        try {
            if (cardNode.has("textAnswer")) {
                var textCard = objectMapper.treeToValue(cardNode, com.service.cardsservice.payload.out.updatecards.TextAnswerCardDTO.class);
                return dbQueryService.updateCard(userId, deckId, cardId, textCard);
            } else if (cardNode.has("choiceAnswers")) {
                var multipleChoiceCardDTO = objectMapper.treeToValue(cardNode, com.service.cardsservice.payload.out.updatecards.MultipleChoiceCardDTO.class);
                return dbQueryService.updateCard(userId, deckId, cardId, multipleChoiceCardDTO);
            } else {
                return HttpStatus.UNPROCESSABLE_ENTITY;
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return HttpStatus.UNPROCESSABLE_ENTITY;
        }
    }
}
