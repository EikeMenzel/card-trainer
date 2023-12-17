package com.service.databaseservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.Image;
import com.service.databaseservice.model.User;
import com.service.databaseservice.model.cards.*;
import com.service.databaseservice.payload.out.CardDTO;
import com.service.databaseservice.payload.savecards.ChoiceAnswerDTO;
import com.service.databaseservice.payload.savecards.MultipleChoiceCardDTO;
import com.service.databaseservice.payload.savecards.TextAnswerCardDTO;
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
        if (optionalCardType.isEmpty())
            return false;

        Optional<Deck> optionalDeck = deckRepository.getDeckByIdAndOwnerId(deckId, userId);
        if (optionalDeck.isEmpty())
            return false;

        try {
            var user = optionalDeck.get().getOwner();
            var image = saveImage(buildImageOutOfBlobHelper(generateBlob(textAnswerCardDTO.getCardDTO().getImageData()), user));
            var card = cardRepository.save(new Card(textAnswerCardDTO.getCardDTO().getQuestion(), image, optionalDeck.get(), optionalCardType.get()));
            image = saveImage(buildImageOutOfBlobHelper(generateBlob(textAnswerCardDTO.getImageData()), user));
            textAnswerCardRepository.save(new TextAnswerCard(card.getId(), textAnswerCardDTO.getTextAnswer(), image));

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
            var user = optionalDeck.get().getOwner();
            var image = saveImage(buildImageOutOfBlobHelper(generateBlob(multipleChoiceCardDTO.getCardDTO().getImageData()), user));
            var card = cardRepository.save(new Card(multipleChoiceCardDTO.getCardDTO().getQuestion(), image, optionalDeck.get(), optionalCardType.get()));
            var multipleChoiceCard = multipleChoiceCardRepository.save(new MultipleChoiceCard(card.getId()));
            choiceAnswerRepository.saveAll(choiceAnswersBuilder(multipleChoiceCardDTO.getChoiceAnswers(), multipleChoiceCard, user));

            return initRepetition(card);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }

    }

    private List<ChoiceAnswer> choiceAnswersBuilder(List<ChoiceAnswerDTO> choiceAnswerDTOList, MultipleChoiceCard multipleChoiceCard, User user) {
        return choiceAnswerDTOList
                .stream()
                .map(choiceAnswerDTO -> {
                    var image = saveImage(buildImageOutOfBlobHelper(generateBlob(choiceAnswerDTO.getImageData()), user));
                    return new ChoiceAnswer(choiceAnswerDTO.getAnswer(), image, choiceAnswerDTO.getIsRightAnswer(), multipleChoiceCard);
                })
                .collect(Collectors.toList());
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
