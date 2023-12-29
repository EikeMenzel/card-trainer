package com.service.cardsservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.cardsservice.payload.out.savecards.MultipleChoiceCardDTO;
import com.service.cardsservice.payload.out.savecards.TextAnswerCardDTO;
import com.service.cardsservice.payload.out.updatecards.ImageDTO;
import com.service.cardsservice.payload.out.updatecards.OperationDTO;
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

    private Optional<MultipartFile> findMultiPartFileByName(MultipartFile[] images, String name) {
        if (images == null)
            return Optional.empty();

        return Arrays.stream(images).filter(multipartFile -> Objects.equals(multipartFile.getOriginalFilename(), name)).findFirst();
    }


    //updateCard
    public HttpStatusCode updateCard(JsonNode cardNode, Long userId, Long deckId, Long cardId, MultipartFile[] images) {
        try {
            if (cardNode.has("textAnswer")) {
                var textCard = objectMapper.treeToValue(cardNode, com.service.cardsservice.payload.out.updatecards.TextAnswerCardDTO.class);
                textCard = updateCardTextAnswerCard(textCard, images);
                return dbQueryService.updateCard(userId, deckId, cardId, textCard);
            } else if (cardNode.has("choiceAnswers")) {
                var multipleChoiceCardDTO = objectMapper.treeToValue(cardNode, com.service.cardsservice.payload.out.updatecards.MultipleChoiceCardDTO.class);
                multipleChoiceCardDTO = updateCardMultipleChoiceCard(multipleChoiceCardDTO, images);
                return dbQueryService.updateCard(userId, deckId, cardId, multipleChoiceCardDTO);
            } else {
                return HttpStatus.UNPROCESSABLE_ENTITY;
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return HttpStatus.UNPROCESSABLE_ENTITY;
        }
    }

    public com.service.cardsservice.payload.out.updatecards.TextAnswerCardDTO updateCardTextAnswerCard(com.service.cardsservice.payload.out.updatecards.TextAnswerCardDTO textAnswerCardDTO, MultipartFile[] images) throws IOException {
        if(textAnswerCardDTO.getCardDTO() != null)
            textAnswerCardDTO.setImageDTO(prepareImageDTO(textAnswerCardDTO.getImageDTO(), images));

        if(textAnswerCardDTO.getCardDTO().getImageDTO() != null)
            textAnswerCardDTO.getCardDTO().setImageDTO(prepareImageDTO(textAnswerCardDTO.getCardDTO().getImageDTO(), images));
        return textAnswerCardDTO;
    }

    public com.service.cardsservice.payload.out.updatecards.MultipleChoiceCardDTO updateCardMultipleChoiceCard(com.service.cardsservice.payload.out.updatecards.MultipleChoiceCardDTO multipleChoiceCardDTO, MultipartFile[] images) throws IOException {
       multipleChoiceCardDTO.getCardDTO().setImageDTO(prepareImageDTO(multipleChoiceCardDTO.getCardDTO().getImageDTO(), images));

        multipleChoiceCardDTO.getChoiceAnswers().forEach(choiceAnswerDTO -> {
            try {
                var updatedImageDTO = prepareImageDTO(choiceAnswerDTO.getImageDTO(), images);
                choiceAnswerDTO.setImageDTO(updatedImageDTO);
            } catch (IOException e) {
                logger.debug("Error processing image for choice answer: " + e.getMessage());
            }
        });

        return multipleChoiceCardDTO;
    }

    public ImageDTO prepareImageDTO(ImageDTO imageDTO, MultipartFile[] images) throws IOException {
        if (imageDTO == null) {
            return null;
        }

        return switch (imageDTO.getOperationDTO()) {
            case CREATE, UPDATE -> handleCreateOrUpdate(imageDTO, images);
            case DELETE -> {
                imageDTO.setImageData(null);
                yield imageDTO;
            }
        };
    }

    private ImageDTO handleCreateOrUpdate(ImageDTO imageDTO, MultipartFile[] images) throws IOException {
        Optional<MultipartFile> fileOptional = findMultiPartFileByName(images, imageDTO.getImagePath());
        if (fileOptional.isPresent() && (imageDTO.getOperationDTO() == OperationDTO.CREATE || imageDTO.getImageId() != null)) {
            imageDTO.setImageData(fileOptional.get().getBytes());
        } else {
            imageDTO.setImageId(null);
            imageDTO.setImageData(null);
        }
        return imageDTO;
    }

}
