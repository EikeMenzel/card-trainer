package com.service.databaseservice.services;

import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.User;
import com.service.databaseservice.model.cards.Card;
import com.service.databaseservice.model.cards.MultipleChoiceCard;
import com.service.databaseservice.model.cards.TextAnswerCard;
import com.service.databaseservice.payload.inc.DeckNameDTO;
import com.service.databaseservice.payload.out.DeckDTO;
import com.service.databaseservice.repository.DeckRepository;
import com.service.databaseservice.repository.cards.CardRepository;
import com.service.databaseservice.repository.cards.MultipleChoiceCardRepository;
import com.service.databaseservice.repository.cards.TextAnswerCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeckService {
    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;
    private final UserService userService;
    private final TextAnswerCardRepository textAnswerCardRepository;
    private final MultipleChoiceCardRepository multipleChoiceCardRepository;
    private final Logger logger = LoggerFactory.getLogger(DeckService.class);

    public DeckService(DeckRepository deckRepository, CardRepository cardRepository, UserService userService, TextAnswerCardRepository textAnswerCardRepository, MultipleChoiceCardRepository multipleChoiceCardRepository) {
        this.deckRepository = deckRepository;
        this.cardRepository = cardRepository;
        this.userService = userService;
        this.textAnswerCardRepository = textAnswerCardRepository;
        this.multipleChoiceCardRepository = multipleChoiceCardRepository;
    }

    public List<DeckDTO> getAllDecksByUserId(Long userId) {
        return deckRepository.getAllByOwnerId(userId)
                .stream()
                .map(deck -> new DeckDTO(deck.getId(), deck.getName()))
                .collect(Collectors.toList());
    }

    public Integer getDeckCountByUserId(Long userId) {
        return deckRepository.getAllByOwnerId(userId).size();
    }

    public Optional<DeckDTO> getDeckByIdAndUserId(Long userId, Long deckId) {
        return deckRepository.getDeckByIdAndOwnerId(deckId, userId)
                .map(deck -> new DeckDTO(deck.getId(), deck.getName()));
    }

    public Optional<String> getDeckNameByIdAndUserId(Long userId, Long deckId) {
        return deckRepository.getDeckByIdAndOwnerId(deckId, userId).map(Deck::getName);
    }
    public Optional<String> getDeckNameById(Long deckId) {
        return deckRepository.findById(deckId).map(Deck::getName);
    }
    public boolean existsByDeckIdAndUserId(Long deckId, Long userId) {
        return deckRepository.existsDeckByIdAndOwnerId(deckId, userId);
    }

    @Transactional
    public boolean createDeck(Long userId, DeckNameDTO deckNameDTO) {
        try {
            Optional<User> user = userService.getUserFromId(userId);
            if (user.isPresent()) {
                deckRepository.save(new Deck(deckNameDTO.deckName(), user.get()));
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteDeck(Long userId, Long deckId) {
        if (deckRepository.existsDeckByIdAndOwnerId(deckId, userId)) {
            deckRepository.deleteById(deckId);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean updateDeckInformation(Long userId, Long deckId, DeckNameDTO deckNameDTO) {
        return deckRepository.getDeckByIdAndOwnerId(deckId, userId)
                .map(deck -> {
                    deck.setName(deckNameDTO.deckName());
                    return true;
                })
                .orElse(false);
    }

    public Optional<Deck> findTopByOwnerIdAndNameOrderByIdDesc(Long userId, String deckName) {
        return deckRepository.findTopByOwnerIdAndNameOrderByIdDesc(userId, deckName);
    }

    @Transactional
    public boolean cloneSharedDeck(Long userId, Long deckId) {
        return deckRepository.findById(deckId)
                .flatMap(deck -> userService.getUserFromId(userId).map(deck::cloneDeck))
                .map(deckRepository::save)
                .map(newDeck -> cloneAllCards(deckId, newDeck))
                .orElse(false);
    }

    private boolean cloneAllCards(Long deckId, Deck newDeck) {
        return cardRepository.getCardsByDeckId(deckId).stream()
                .allMatch(card -> cloneCardWithDependencies(card, newDeck));
    }

    private boolean cloneCardWithDependencies(Card card, Deck newDeck) {
        try {
            var newCard = cardRepository.save(card.cloneWithDifferentDeck(newDeck));
            return switch (card.getCardType().getType()) {
                case "BASIC" -> saveClonedTextAnswerCard(newCard, card);
                case "MULTIPLE_CHOICE" -> saveClonedMultipleChoiceCard(newCard, card);
                default -> false;
            };
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    private boolean saveClonedTextAnswerCard(Card newCard, Card oldCard) {
        try {
            Optional<TextAnswerCard> textAnswerCardOptional = textAnswerCardRepository.getTextAnswerCardById(oldCard.getId());
            if (textAnswerCardOptional.isEmpty())
                return false;
            var newTextAnswerCard = textAnswerCardOptional.get().cloneTextAnswercard(newCard.getId());
            textAnswerCardRepository.save(newTextAnswerCard);
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    private boolean saveClonedMultipleChoiceCard(Card newCard, Card oldCard) {
        try {
            Optional<MultipleChoiceCard> multipleChoiceCard = multipleChoiceCardRepository.findById(oldCard.getId());
            if (multipleChoiceCard.isEmpty())
                return false;
            var newMultipleChoiceCard = multipleChoiceCard.get().cloneMultipleChoiceCard(newCard.getId());
            multipleChoiceCardRepository.save(newMultipleChoiceCard);
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }
}
