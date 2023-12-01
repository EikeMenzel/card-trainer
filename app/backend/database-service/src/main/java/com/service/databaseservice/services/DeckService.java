package com.service.databaseservice.services;

import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.User;
import com.service.databaseservice.model.cards.CardType;
import com.service.databaseservice.payload.inc.DeckNameDTO;
import com.service.databaseservice.payload.out.DeckDTO;
import com.service.databaseservice.payload.out.export.CardDTO;
import com.service.databaseservice.payload.out.export.CardExportDTO;
import com.service.databaseservice.payload.out.export.TextAnswerDTO;
import com.service.databaseservice.repository.DeckRepository;
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
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(DeckService.class);

    public DeckService(DeckRepository deckRepository, UserService userService) {
        this.deckRepository = deckRepository;
        this.userService = userService;
    }

    public List<DeckDTO> getAllDecksByUserId(Long userId) {
        return deckRepository.getAllByOwnerId(userId)
                .stream()
                .map(deck -> new DeckDTO(deck.getId(), deck.getName()))
                .collect(Collectors.toList());
    }

    public Optional<DeckDTO> getDeckByIdAndUserId(Long userId, Long deckId) {
        return deckRepository.getDeckByIdAndOwnerId(deckId, userId)
                .map(deck -> new DeckDTO(deck.getId(), deck.getName()));
    }

    public Optional<String> getDeckNameByIdAndUserId(Long userId, Long deckId) {
        return deckRepository.getDeckByIdAndOwnerId(deckId, userId).map(Deck::getName);
    }
    
    public Boolean existsByDeckIdAndUserId(Long deckId, Long userId) {
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
}
