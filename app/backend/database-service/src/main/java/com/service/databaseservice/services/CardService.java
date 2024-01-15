package com.service.databaseservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.Image;
import com.service.databaseservice.model.cards.*;
import com.service.databaseservice.payload.out.CardDTO;
import com.service.databaseservice.payload.savecard.ChoiceAnswerDTO;
import com.service.databaseservice.payload.savecard.MultipleChoiceCardDTO;
import com.service.databaseservice.payload.savecard.TextAnswerCardDTO;
import com.service.databaseservice.repository.DeckRepository;
import com.service.databaseservice.repository.ImageRepository;
import com.service.databaseservice.repository.cards.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardService {
    private final DeckRepository deckRepository;
    private final CardTypeRepository cardTypeRepository;
    private final CardRepository cardRepository;
    private final TextAnswerCardRepository textAnswerCardRepository;
    private final MultipleChoiceCardRepository multipleChoiceCardRepository;
    private final ChoiceAnswerRepository choiceAnswerRepository;
    private final ObjectMapper objectMapper;
    private final RepetitionService repetitionService;
    private final ImageRepository imageRepository;

    private final Logger logger = LoggerFactory.getLogger(CardService.class);

    public CardService(DeckRepository deckRepository, CardTypeRepository cardTypeRepository, CardRepository cardRepository, TextAnswerCardRepository textAnswerCardRepository, MultipleChoiceCardRepository multipleChoiceCardRepository, ChoiceAnswerRepository choiceAnswerRepository, ObjectMapper objectMapper, RepetitionService repetitionService, ImageRepository imageRepository) {
        this.deckRepository = deckRepository;
        this.cardTypeRepository = cardTypeRepository;
        this.cardRepository = cardRepository;
        this.textAnswerCardRepository = textAnswerCardRepository;
        this.multipleChoiceCardRepository = multipleChoiceCardRepository;
        this.choiceAnswerRepository = choiceAnswerRepository;
        this.objectMapper = objectMapper;
        this.repetitionService = repetitionService;
        this.imageRepository = imageRepository;
    }

    public boolean doesCardBelongToOwner(Long cardId, Long userId) {
        return cardRepository.findById(cardId).map(card -> Objects.equals(card.getDeck().getOwner().getId(), userId)).orElse(false);
    }

    public boolean doesCardBelongToOwnerAndDeck(Long userId, Long deckId, Long cardId) {
        Optional<Deck> optionalDeck = deckRepository.getDeckByIdAndOwnerId(deckId, userId);

        return optionalDeck.map(deck -> cardRepository.findById(cardId).map(card -> card.getDeck().equals(deck)).orElse(false)).orElse(false);
    }

    public Integer getCardAmountFromDeckId(Long deckId) {
        return cardRepository.countCardsByDeckId(deckId);
    }

    public List<Card> getCardsByDeckId(Long deckId) {
        return cardRepository.getCardsByDeckId(deckId);
    }

    public List<CardDTO> getCardsFromDeckId(Long deckId) {
        return cardRepository.getCardsByDeckId(deckId)
                .stream()
                .map(card -> new CardDTO(card.getId(), card.getQuestion(), card.getCardType().getType()))
                .collect(Collectors.toList());
    }


    //saveCard
    @Transactional
    public boolean saveCard(JsonNode cardNode, Long userId, Long deckId) {
        try {
            if (cardNode.has("textAnswer")) {
                var textCard = objectMapper.treeToValue(cardNode, TextAnswerCardDTO.class);
                return saveTextAnswerCard(userId, deckId, textCard);
            } else if (cardNode.has("choiceAnswers")) {
                var multipleChoiceCardDTO = objectMapper.treeToValue(cardNode, MultipleChoiceCardDTO.class);
                return saveMultipleChoiceCard(userId, deckId, multipleChoiceCardDTO);
            } else {
                return false; // invalid type
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    private boolean saveTextAnswerCard(Long userId, Long deckId, TextAnswerCardDTO textAnswerCardDTO) {
        Optional<CardType> optionalCardType = cardTypeRepository.getCardTypesByType("BASIC");
        Optional<Deck> optionalDeck = deckRepository.getDeckByIdAndOwnerId(deckId, userId);
        if (optionalDeck.isEmpty() || optionalCardType.isEmpty())
            return false;

        try {
            var card = cardRepository.save(new Card(textAnswerCardDTO.getCardDTO().getQuestion(), getImageFromImageId(textAnswerCardDTO.getCardDTO().getImageId()), optionalDeck.get(), optionalCardType.get()));
            textAnswerCardRepository.save(new TextAnswerCard(card.getId(), textAnswerCardDTO.getTextAnswer(), getImageFromImageId(textAnswerCardDTO.getImageId())));

            return initRepetition(card);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    private boolean saveMultipleChoiceCard(Long userId, Long deckId, MultipleChoiceCardDTO multipleChoiceCardDTO) {
        Optional<CardType> optionalCardType = cardTypeRepository.getCardTypesByType("MULTIPLE_CHOICE");
        if (optionalCardType.isEmpty())
            return false;

        Optional<Deck> optionalDeck = deckRepository.getDeckByIdAndOwnerId(deckId, userId);
        if (optionalDeck.isEmpty())
            return false;

        try {
            var card = cardRepository.save(new Card(multipleChoiceCardDTO.getCardDTO().getQuestion(), getImageFromImageId(multipleChoiceCardDTO.getCardDTO().getImageId()), optionalDeck.get(), optionalCardType.get()));
            var multipleChoiceCard = multipleChoiceCardRepository.save(new MultipleChoiceCard(card.getId()));
            choiceAnswerRepository.saveAll(choiceAnswersBuilder(multipleChoiceCardDTO.getChoiceAnswers(), multipleChoiceCard));

            return initRepetition(card);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }

    }

    private List<ChoiceAnswer> choiceAnswersBuilder(List<ChoiceAnswerDTO> choiceAnswerDTOList, MultipleChoiceCard multipleChoiceCard) {
        return choiceAnswerDTOList
                .stream()
                .map(choiceAnswerDTO -> new ChoiceAnswer(choiceAnswerDTO.getAnswer(), getImageFromImageId(choiceAnswerDTO.getImageId()), choiceAnswerDTO.getIsRightAnswer(), multipleChoiceCard))
                .collect(Collectors.toList());
    }

    private Image getImageFromImageId(Long imageId) {
        if (imageId == null)
            return null;

        return imageRepository.findById(imageId).orElse(null);
    }

    //updateCard
    @Transactional
    public boolean updateCard(JsonNode cardNode, Long userId, Long cardId) {
        try {
            if (cardNode.has("textAnswer")) {
                var textCard = objectMapper.treeToValue(cardNode, com.service.databaseservice.payload.inc.updatecard.TextAnswerCardDTO.class);
                return updateTextAnswerCard(cardId, textCard, userId);
            } else if (cardNode.has("choiceAnswers")) {
                var multipleChoiceCard = objectMapper.treeToValue(cardNode, com.service.databaseservice.payload.inc.updatecard.MultipleChoiceCardDTO.class);
                return updateMultipleChoiceCard(cardId, multipleChoiceCard, userId);
            } else {
                return false; // invalid type
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean updateMultipleChoiceCard(Long cardId, com.service.databaseservice.payload.inc.updatecard.MultipleChoiceCardDTO multipleChoiceCardDTO, Long userId) {
        Optional<Card> cardOptional = cardRepository.getCardById(cardId);
        Optional<MultipleChoiceCard> multipleChoiceCardOptional = multipleChoiceCardRepository.findById(multipleChoiceCardDTO.getMultipleChoiceCardId());
        if (cardOptional.isEmpty() || multipleChoiceCardOptional.isEmpty())
            return false;

        try {
            var card = cardOptional.get();
            var multipleChoiceCard = multipleChoiceCardOptional.get();
            boolean updateBaseCard = updateBaseCard(multipleChoiceCardDTO.getCardDTO(), card, card.getDeck().getOwner().getId());
            boolean updateChoiceAnswers = updateChoiceAnswers(multipleChoiceCardDTO.getChoiceAnswers(), multipleChoiceCard, userId);

            return updateBaseCard && updateChoiceAnswers;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    List<Long> getMissingChoiceAnswerIds(List<com.service.databaseservice.payload.inc.updatecard.ChoiceAnswerDTO> choiceAnswerDTOs, List<ChoiceAnswer> choiceAnswers) {
        Set<Long> dtoIds = choiceAnswerDTOs.stream()
                .filter(Objects::nonNull) // Filter out null IDs; Only used for Creating new ChoiceAnswer
                .map(com.service.databaseservice.payload.inc.updatecard.ChoiceAnswerDTO::getChoiceAnswerId)
                .collect(Collectors.toSet());

        // Filter choiceAnswers to find missing IDs
        return choiceAnswers.stream()
                .map(ChoiceAnswer::getId)
                .filter(id -> !dtoIds.contains(id))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean updateChoiceAnswers(List<com.service.databaseservice.payload.inc.updatecard.ChoiceAnswerDTO> choiceAnswerDTOs, MultipleChoiceCard multipleChoiceCard, Long userId) {
        try {
            //delete all Ids that are not sent
            List<Long> excludedIds = getMissingChoiceAnswerIds(choiceAnswerDTOs, multipleChoiceCard.getChoiceAnswerList());
            excludedIds.forEach(choiceAnswerRepository::deleteById);


            //delete answers that have not been in the body (we assume in this case that the user wants to delete them)
            deleteChoiceAnswers(choiceAnswerDTOs, multipleChoiceCard);

            boolean createChoiceAnswerResult = isCreateChoiceAnswerResult(choiceAnswerDTOs, multipleChoiceCard, userId);

            //actual updateAnswers
            return createChoiceAnswerResult && choiceAnswerDTOs.stream()
                    .filter(choiceAnswerDTO -> choiceAnswerDTO.getChoiceAnswerId() != null)
                    .filter(choiceAnswerDTO -> !excludedIds.contains(choiceAnswerDTO.getChoiceAnswerId()))
                    .allMatch(choiceAnswerDTO -> {
                        Optional<ChoiceAnswer> choiceAnswerOptional = choiceAnswerRepository.findById(choiceAnswerDTO.getChoiceAnswerId());
                        if (choiceAnswerOptional.isEmpty())
                            return false;

                        var choiceAnswer = choiceAnswerOptional.get();

                        try {
                            if (choiceAnswerDTO.getImageId() == null) {
                                choiceAnswerRepository.save(choiceAnswer.updateChoiceAnswer(choiceAnswerDTO.getAnswer(), choiceAnswerDTO.getIsRightAnswer()));
                            } else {
                                Optional<Image> imageOptional = imageRepository.getImageByIdAndUserId(choiceAnswerDTO.getImageId(), userId);
                                if (imageOptional.isEmpty())
                                    return false;

                                choiceAnswerRepository.save(choiceAnswer.updateChoiceAnswer(choiceAnswer.getAnswer(), imageOptional.get(), choiceAnswerDTO.getIsRightAnswer()));
                            }
                            return true;
                        } catch (Exception e) {
                            logger.debug(e.getMessage());
                            return false;
                        }
                    });
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    @Transactional
    public void deleteChoiceAnswers(List<com.service.databaseservice.payload.inc.updatecard.ChoiceAnswerDTO> choiceAnswerDTOs, MultipleChoiceCard multipleChoiceCard) {
        List<ChoiceAnswer> allByMultipleChoiceCardId = choiceAnswerRepository.getAllByMultipleChoiceCardId(multipleChoiceCard.getId());
        var idsToRemoveList = allByMultipleChoiceCardId.stream()
                .filter(choiceAnswer -> choiceAnswerDTOs.stream()
                        .noneMatch(choiceAnswerDTO -> Objects.equals(choiceAnswerDTO.getChoiceAnswerId(), choiceAnswer.getId()))
                )
                .map(ChoiceAnswer::getId)
                .toList();

        if(idsToRemoveList.isEmpty())
            return;

        try {
            choiceAnswerRepository.deleteAllByIds(idsToRemoveList);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }

    @Transactional
    public boolean isCreateChoiceAnswerResult(List<com.service.databaseservice.payload.inc.updatecard.ChoiceAnswerDTO> choiceAnswerDTOs, MultipleChoiceCard multipleChoiceCard, Long userId) {
        //create choiceAnswers
        return choiceAnswerDTOs.stream()
                .filter(choiceAnswerDTO -> choiceAnswerDTO.getChoiceAnswerId() == null)
                .allMatch(choiceAnswerDTO -> {
                    if (choiceAnswerDTO.getImageId() == null) {
                        choiceAnswerRepository.save(new ChoiceAnswer(choiceAnswerDTO.getAnswer(), null, choiceAnswerDTO.getIsRightAnswer(), multipleChoiceCard));
                    } else {
                        Optional<Image> imageOptional = imageRepository.getImageByIdAndUserId(choiceAnswerDTO.getImageId(), userId);
                        if (imageOptional.isEmpty())
                            return false;

                        choiceAnswerRepository.save(new ChoiceAnswer(choiceAnswerDTO.getAnswer(), imageOptional.get(), choiceAnswerDTO.getIsRightAnswer(), multipleChoiceCard));
                    }
                    return true;
                });
    }

    @Transactional
    public boolean updateTextAnswerCard(Long cardId, com.service.databaseservice.payload.inc.updatecard.TextAnswerCardDTO textAnswerCardDTO, Long userId) {
        Optional<Card> cardOptional = cardRepository.getCardById(cardId);
        Optional<TextAnswerCard> textAnswerCardOptional = textAnswerCardRepository.findById(textAnswerCardDTO.getTextAnswerCardId());
        if (cardOptional.isEmpty() || textAnswerCardOptional.isEmpty())
            return false;

        try {
            var card = cardOptional.get();
            var textAnswerCard = textAnswerCardOptional.get();
            boolean updateBaseCard = updateBaseCard(textAnswerCardDTO.getCardDTO(), card, userId);
            boolean updateAnswerCard = updateAnswerCard(textAnswerCardDTO, textAnswerCard, card);

            return updateBaseCard && updateAnswerCard;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    //For TextAnswerCard
    @Transactional
    public boolean updateAnswerCard(com.service.databaseservice.payload.inc.updatecard.TextAnswerCardDTO textAnswerCardDTO, TextAnswerCard textAnswerCard, Card card) {
        try {
            if (textAnswerCardDTO.getImageId() == null) {
                textAnswerCardRepository.save(textAnswerCard.updateTextAnswerCard(textAnswerCardDTO.getTextAnswer()));
            } else {
                Optional<Image> image = imageRepository.getImageByIdAndUserId(textAnswerCardDTO.getImageId(), card.getDeck().getOwner().getId());
                if (image.isEmpty())
                    return false;

                textAnswerCardRepository.save(textAnswerCard.updateTextAnswerCard(textAnswerCardDTO.getTextAnswer(), image.get()));
            }
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    //For CardDTO
    public boolean updateBaseCard(com.service.databaseservice.payload.inc.updatecard.CardDTO cardDTO, Card card, Long userId) {
        try {
            if (cardDTO.getImageId() == null) {
                cardRepository.save(card.updateCard(cardDTO.getQuestion()));
            } else {
                Optional<Image> image = imageRepository.getImageByIdAndUserId(cardDTO.getImageId(), userId);
                if (image.isEmpty())
                    return false;

                cardRepository.save(card.updateCard(cardDTO.getQuestion(), image.get()));
            }
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    private boolean initRepetition(Card card) {
        return repetitionService.initRepetition(card, card.getDeck().getOwner());
    }

    @Transactional
    public boolean deleteCard(Long cardId) {
        if (cardRepository.getCardById(cardId).isEmpty())
            return false;

        cardRepository.deleteById(cardId);
        return true;
    }

    public Object getCardDetails(Long cardId) {
        return cardRepository.findById(cardId)
                .map(this::convertCardToDTO)
                .orElse(null);
    }

    public Optional<Object> getOldestCardToLearn(Long deckId) {
        Optional<Card> cardOptional = cardRepository.findOldestCardToLearn(deckId);
        return cardOptional.map(card -> getCardDetails(card.getId()));
    }

    private Object convertCardToDTO(Card card) {
        var cardDTO = new com.service.databaseservice.payload.out.getcarddetails.CardDTO(card.getId(), card.getQuestion(),
                getImageIdFromImage(card.getImageData()),
                card.getCardType().getType());

        return switch (card.getCardType().getType()) {
            case "BASIC" -> getTextAnswerCardDTO(card.getId(), cardDTO);
            case "MULTIPLE_CHOICE" -> getMultipleChoiceCardDTO(card.getId(), cardDTO);
            default -> null;
        };
    }

    private com.service.databaseservice.payload.out.getcarddetails.TextAnswerCardDTO getTextAnswerCardDTO(Long cardId, com.service.databaseservice.payload.out.getcarddetails.CardDTO cardDTO) {
        return textAnswerCardRepository.findById(cardId)
                .map(textAnswerCard -> new com.service.databaseservice.payload.out.getcarddetails.TextAnswerCardDTO(cardDTO,
                        textAnswerCard.getId(),
                        textAnswerCard.getAnswer(),
                        getImageIdFromImage(textAnswerCard.getImageData())))
                .orElse(null);
    }

    private com.service.databaseservice.payload.out.getcarddetails.MultipleChoiceCardDTO getMultipleChoiceCardDTO(Long cardId, com.service.databaseservice.payload.out.getcarddetails.CardDTO cardDTO) {
        return multipleChoiceCardRepository.findById(cardId)
                .map(multipleChoiceCard -> createMultipleChoiceCardDTO(cardDTO, multipleChoiceCard))
                .orElse(null);
    }

    private com.service.databaseservice.payload.out.getcarddetails.MultipleChoiceCardDTO createMultipleChoiceCardDTO(com.service.databaseservice.payload.out.getcarddetails.CardDTO cardDTO, MultipleChoiceCard multipleChoiceCard) {
        var choiceAnswerDTOS = multipleChoiceCard.getChoiceAnswerList()
                .stream()
                .map(this::createChoiceAnswerDTO)
                .toList();
        return new com.service.databaseservice.payload.out.getcarddetails.MultipleChoiceCardDTO(cardDTO, multipleChoiceCard.getId(), choiceAnswerDTOS);
    }

    private com.service.databaseservice.payload.out.getcarddetails.ChoiceAnswerDTO createChoiceAnswerDTO(ChoiceAnswer choiceAnswer) {
        return new com.service.databaseservice.payload.out.getcarddetails.ChoiceAnswerDTO(choiceAnswer.getId(), choiceAnswer.getAnswer(),
                choiceAnswer.getCorrect(), getImageIdFromImage(choiceAnswer.getImageData()));
    }

    private Long getImageIdFromImage(Image image) {
        if (image == null)
            return null;
        return image.getId();
    }
}
