package com.service.databaseservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.Image;
import com.service.databaseservice.model.User;
import com.service.databaseservice.model.cards.*;
import com.service.databaseservice.payload.out.CardDTO;
import com.service.databaseservice.payload.savecard.ChoiceAnswerDTO;
import com.service.databaseservice.payload.savecard.MultipleChoiceCardDTO;
import com.service.databaseservice.payload.savecard.TextAnswerCardDTO;
import com.service.databaseservice.repository.DeckRepository;
import com.service.databaseservice.repository.ImageRepository;
import com.service.databaseservice.repository.cards.*;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Blob;
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
    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;
    private final RepetitionService repetitionService;
    private final ImageRepository imageRepository;
    private final Logger logger = LoggerFactory.getLogger(CardService.class);

    public CardService(DeckRepository deckRepository, CardTypeRepository cardTypeRepository, CardRepository cardRepository, TextAnswerCardRepository textAnswerCardRepository, MultipleChoiceCardRepository multipleChoiceCardRepository, ChoiceAnswerRepository choiceAnswerRepository, EntityManager entityManager, ObjectMapper objectMapper, RepetitionService repetitionService, ImageRepository imageRepository) {
        this.deckRepository = deckRepository;
        this.cardTypeRepository = cardTypeRepository;
        this.cardRepository = cardRepository;
        this.textAnswerCardRepository = textAnswerCardRepository;
        this.multipleChoiceCardRepository = multipleChoiceCardRepository;
        this.choiceAnswerRepository = choiceAnswerRepository;
        this.entityManager = entityManager;
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
        if(imageId == null)
            return null;

        return imageRepository.findById(imageId).orElse(null);
    }

    //updateCard
    @Transactional
    public boolean updateCard(JsonNode cardNode, Long userId, Long deckId, Long cardId) {
        try {
            if (cardNode.has("textAnswer")) {
                var textCard = objectMapper.treeToValue(cardNode, com.service.databaseservice.payload.inc.updatecards.TextAnswerCardDTO.class);
                return updateTextAnswerCard(cardId, textCard);
            } else if (cardNode.has("choiceAnswers")) {
                var multipleChoiceCard = objectMapper.treeToValue(cardNode, com.service.databaseservice.payload.inc.updatecards.MultipleChoiceCardDTO.class);
                return updateMultipleChoiceCard(cardId, multipleChoiceCard);
            } else {
                return false; // invalid type
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public boolean updateMultipleChoiceCard(Long cardId, com.service.databaseservice.payload.inc.updatecards.MultipleChoiceCardDTO multipleChoiceCardDTO) {
        Optional<Card> cardOptional = cardRepository.getCardById(cardId);
        Optional<MultipleChoiceCard> multipleChoiceCardOptional = multipleChoiceCardRepository.findById(multipleChoiceCardDTO.getMultipleChoiceCardId());
        if (cardOptional.isEmpty() || multipleChoiceCardOptional.isEmpty())
            return false;

        try {
            var card = cardOptional.get();
            var multipleChoiceCard = multipleChoiceCardOptional.get();
            boolean updateBaseCard = updateBaseCard(multipleChoiceCardDTO.getCardDTO(), card);
            boolean updateChoiceAnswers = updateChoiceAnswers(multipleChoiceCardDTO.getChoiceAnswers(), card, multipleChoiceCard);

            return updateBaseCard && updateChoiceAnswers;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    private List<Long> getMissingChoiceAnswerIds(List<com.service.databaseservice.payload.inc.updatecards.ChoiceAnswerDTO> choiceAnswerDTOs, List<ChoiceAnswer> choiceAnswers) {
        Set<Long> dtoIds = choiceAnswerDTOs.stream()
                .filter(Objects::nonNull) // Filter out null IDs; Only used for Creating new ChoiceAnswer
                .map(com.service.databaseservice.payload.inc.updatecards.ChoiceAnswerDTO::getChoiceAnswerId)
                .collect(Collectors.toSet());

        // Filter choiceAnswers to find missing IDs
        return choiceAnswers.stream()
                .map(ChoiceAnswer::getId)
                .filter(id -> !dtoIds.contains(id))
                .collect(Collectors.toList());
    }

    public boolean updateChoiceAnswers(List<com.service.databaseservice.payload.inc.updatecards.ChoiceAnswerDTO> choiceAnswerDTOs, Card card, MultipleChoiceCard multipleChoiceCard) {
        try {
            //delete all Ids that are not sent
            List<Long> excludedIds = getMissingChoiceAnswerIds(choiceAnswerDTOs, multipleChoiceCard.getChoiceAnswerList());
            excludedIds.forEach(choiceAnswerRepository::deleteById);

            //create choiceAnswers
            choiceAnswerDTOs.stream()
                    .filter(choiceAnswerDTO -> choiceAnswerDTO.getChoiceAnswerId() == null)
                    .forEach(choiceAnswerDTO -> {
                        if (choiceAnswerDTO.getImageDTO() != null) {
                            var image = saveImage(buildImageOutOfBlobHelper(generateBlob(choiceAnswerDTO.getImageDTO().getImageData()), card.getDeck().getOwner()));
                            choiceAnswerRepository.save(new ChoiceAnswer(choiceAnswerDTO.getAnswer(), image, choiceAnswerDTO.getIsRightAnswer(), multipleChoiceCard));
                        } else {
                            choiceAnswerRepository.save(new ChoiceAnswer(choiceAnswerDTO.getAnswer(), null, choiceAnswerDTO.getIsRightAnswer(), multipleChoiceCard));
                        }
                    });


            //actual updateAnswers
            return choiceAnswerDTOs.stream()
                    .filter(choiceAnswerDTO -> choiceAnswerDTO.getChoiceAnswerId() != null)
                    .filter(choiceAnswerDTO -> !excludedIds.contains(choiceAnswerDTO.getChoiceAnswerId()))
                    .allMatch(choiceAnswerDTO -> {
                        Optional<ChoiceAnswer> choiceAnswerOptional = choiceAnswerRepository.findById(choiceAnswerDTO.getChoiceAnswerId());
                        if (choiceAnswerOptional.isEmpty())
                            return false;

                        var choiceAnswer = choiceAnswerOptional.get();

                        if (choiceAnswerDTO.getImageDTO() == null) {
                            choiceAnswerRepository.save(choiceAnswer.updateChoiceAnswer(choiceAnswerDTO.getAnswer(), choiceAnswerDTO.getIsRightAnswer()));
                            return true;
                        } else {
                            return switch (choiceAnswerDTO.getImageDTO().getOperationDTO()) {
                                case CREATE -> {
                                    var image = imageRepository.save(new Image(generateBlob(choiceAnswerDTO.getImageDTO().getImageData()), card.getDeck().getOwner()));
                                    choiceAnswerRepository.save(choiceAnswer.updateChoiceAnswer(choiceAnswerDTO.getAnswer(), image, choiceAnswerDTO.getIsRightAnswer()));
                                    yield true;
                                }
                                case UPDATE -> {
                                    Optional<Image> imageOptional = imageRepository.getImageByIdAndUserId(choiceAnswerDTO.getImageDTO().getImageId(), card.getDeck().getOwner().getId());
                                    if (imageOptional.isEmpty())
                                        yield false;

                                    var image = imageRepository.save(imageOptional.get().updateImage(generateBlob(choiceAnswerDTO.getImageDTO().getImageData())));
                                    choiceAnswerRepository.save(choiceAnswer.updateChoiceAnswer(choiceAnswerDTO.getAnswer(), image, choiceAnswerDTO.getIsRightAnswer()));
                                    yield true;
                                }
                                case DELETE -> {
                                    imageRepository.deleteById(choiceAnswerDTO.getImageDTO().getImageId());
                                    choiceAnswerRepository.save(choiceAnswer.updateChoiceAnswer(choiceAnswerDTO.getAnswer(), null, choiceAnswerDTO.getIsRightAnswer()));
                                    yield true;
                                }
                            };
                        }
                    });
        } catch (Exception e) {
            logger.info(e.getMessage());
            return false;
        }
    }

    public boolean updateTextAnswerCard(Long cardId, com.service.databaseservice.payload.inc.updatecards.TextAnswerCardDTO textAnswerCardDTO) {
        Optional<Card> cardOptional = cardRepository.getCardById(cardId);
        Optional<TextAnswerCard> textAnswerCardOptional = textAnswerCardRepository.findById(textAnswerCardDTO.getTextAnswerCardId());
        if (cardOptional.isEmpty() || textAnswerCardOptional.isEmpty())
            return false;

        try {
            var card = cardOptional.get();
            var textAnswerCard = textAnswerCardOptional.get();
            boolean updateBaseCard = updateBaseCard(textAnswerCardDTO.getCardDTO(), card);
            boolean updateAnswerCard = updateAnswerCard(textAnswerCardDTO, textAnswerCard, card);
            return updateBaseCard && updateAnswerCard;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public boolean updateAnswerCard(com.service.databaseservice.payload.inc.updatecards.TextAnswerCardDTO textAnswerCardDTO, TextAnswerCard textAnswerCard, Card card) {
        try {
            if (textAnswerCardDTO.getImageDTO() == null) {
                textAnswerCardRepository.save(textAnswerCard.updateTextAnswerCard(textAnswerCard.getAnswer()));
                return true;
            }

            return switch (textAnswerCardDTO.getImageDTO().getOperationDTO()) {
                case CREATE -> {
                    var image = imageRepository.save(new Image(generateBlob(textAnswerCardDTO.getImageDTO().getImageData()), card.getDeck().getOwner()));
                    textAnswerCardRepository.save(textAnswerCard.updateTextAnswerCard(textAnswerCardDTO.getTextAnswer(), image));
                    yield true;
                }
                case UPDATE -> {
                    Optional<Image> imageOptional = imageRepository.getImageByIdAndUserId(textAnswerCardDTO.getImageDTO().getImageId(), card.getDeck().getOwner().getId());
                    if (imageOptional.isEmpty())
                        yield false;

                    var image = imageRepository.save(imageOptional.get().updateImage(generateBlob(textAnswerCardDTO.getImageDTO().getImageData())));
                    textAnswerCardRepository.save(textAnswerCard.updateTextAnswerCard(textAnswerCardDTO.getTextAnswer(), image));
                    yield true;
                }
                case DELETE -> {
                    imageRepository.deleteById(textAnswerCardDTO.getImageDTO().getImageId());
                    textAnswerCardRepository.save(textAnswerCard.updateTextAnswerCard(textAnswerCardDTO.getTextAnswer(), null));
                    yield true;
                }
            };
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public boolean updateBaseCard(com.service.databaseservice.payload.inc.updatecards.CardDTO cardDTO, Card card) {
        try {
            if (cardDTO.getImageDTO() == null) {
                cardRepository.save(card.updateCard(cardDTO.getQuestion()));
                return true;
            }

            return switch (cardDTO.getImageDTO().getOperationDTO()) {
                case CREATE -> {
                    var image = imageRepository.save(new Image(generateBlob(cardDTO.getImageDTO().getImageData()), card.getDeck().getOwner()));
                    cardRepository.save(card.updateCard(cardDTO.getQuestion(), image));
                    yield true;
                }
                case UPDATE -> {
                    Optional<Image> imageOptional = imageRepository.getImageByIdAndUserId(cardDTO.getImageDTO().getImageId(), card.getDeck().getOwner().getId());
                    if (imageOptional.isEmpty())
                        yield false;

                    var image = imageRepository.save(imageOptional.get().updateImage(generateBlob(cardDTO.getImageDTO().getImageData())));
                    cardRepository.save(card.updateCard(cardDTO.getQuestion(), image));
                    yield true;
                }
                case DELETE -> {
                    imageRepository.deleteById(cardDTO.getImageDTO().getImageId());
                    cardRepository.save(card.updateCard(cardDTO.getQuestion(), null));
                    yield true;
                }
            };
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    private Blob generateBlob(byte[] data) {
        if (data == null)
            return null;

        try (var session = entityManager.unwrap(Session.class)) {
            return session.getLobHelper().createBlob(data);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    private boolean initRepetition(Card card) {
        return repetitionService.initRepetition(card, card.getDeck().getOwner());
    }

    private Image saveImage(Image image) {
        try {
            return imageRepository.save(image);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    private Image buildImageOutOfBlobHelper(Blob blob, User user) {
        return new Image(blob, user);
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
