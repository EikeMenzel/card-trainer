package com.service.databaseservice.services;

import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.User;
import com.service.databaseservice.model.cards.Card;
import com.service.databaseservice.model.sessions.LearnSession;
import com.service.databaseservice.model.sessions.PeekSession;
import com.service.databaseservice.model.sessions.PeekSessionCards;
import com.service.databaseservice.model.sessions.StatusType;
import com.service.databaseservice.payload.inc.learnsession.StatusTypeDTO;
import com.service.databaseservice.repository.DeckRepository;
import com.service.databaseservice.repository.UserRepository;
import com.service.databaseservice.repository.cards.CardRepository;
import com.service.databaseservice.repository.sessions.PeekSessionCardsRepository;
import com.service.databaseservice.repository.sessions.PeekSessionRepository;
import com.service.databaseservice.repository.sessions.StatusTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PeekSessionService {
    private final StatusTypeRepository statusTypeRepository;
    private final UserRepository userRepository;
    private final DeckRepository deckRepository;
    private final PeekSessionRepository peekSessionRepository;
    private final PeekSessionCardsRepository peekSessionCardsRepository;
    private final CardService cardService;
    private final CardRepository cardRepository;
    private final Random random = new Random();
    private final Logger logger = LoggerFactory.getLogger(PeekSessionService.class);
    public PeekSessionService(StatusTypeRepository statusTypeRepository, UserRepository userRepository, DeckRepository deckRepository, PeekSessionRepository peekSessionRepository, PeekSessionCardsRepository peekSessionCardsRepository, CardService cardService, CardRepository cardRepository) {
        this.statusTypeRepository = statusTypeRepository;
        this.userRepository = userRepository;
        this.deckRepository = deckRepository;
        this.peekSessionRepository = peekSessionRepository;
        this.peekSessionCardsRepository = peekSessionCardsRepository;
        this.cardService = cardService;
        this.cardRepository = cardRepository;
    }

    @Transactional
    public Optional<Long> createPeekSession(Long userId, Long deckId) {
        try {
            Optional<StatusType> statusTypeOptional = statusTypeRepository.findById(1L); // Entry: Not Finished
            Optional<User> userOptional = userRepository.findById(userId);
            Optional<Deck> deckOptional = deckRepository.findById(deckId);

            if (statusTypeOptional.isEmpty() || userOptional.isEmpty() || deckOptional.isEmpty())
                return Optional.empty();

            var peekSession = peekSessionRepository.save(new PeekSession(userOptional.get(), deckOptional.get(), statusTypeOptional.get()));
            return peekSession.getId().describeConstable();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public boolean doesPeekSessionFromUserExist(Long userId, Long peekSessionId) {
        return peekSessionRepository.getPeekSessionByIdAndUserId(peekSessionId, userId).isPresent();
    }

    public Optional<Object> getRandomCardToLearn(Long peekSessionId) {
        try {
            Optional<PeekSession> peekSessionOptional = peekSessionRepository.findById(peekSessionId);
            if(peekSessionOptional.isEmpty())
                return Optional.empty();

            Long deckId = peekSessionOptional.get().getDeck().getId();

            List<Card> deckCards = cardRepository.getCardsByDeckId(deckId);
            deckCards.removeAll(cardsAlreadyShown(peekSessionId));

            return Optional.ofNullable(cardService.getCardDetails(deckCards.get(random.nextInt(deckCards.size())).getId()));
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    private List<Card> cardsAlreadyShown(Long peekSessionId) {
        return peekSessionCardsRepository.getAllByPeekSessionId(peekSessionId)
                .stream()
                .map(PeekSessionCards::getCard)
                .toList();
    }

    @Transactional
    public boolean updateStatusTypeInPeekSession(Long peekSessionId, StatusTypeDTO statusTypeDTO) {
        Optional<PeekSession> peekSessionOptional = peekSessionRepository.findById(peekSessionId);
        Optional<StatusType> statusTypeOptional = statusTypeRepository.findById(statusTypeDTO.getFieldId());

        if(peekSessionOptional.isEmpty() || statusTypeOptional.isEmpty())
            return false;

        try {
            peekSessionRepository.save(
                    peekSessionOptional.get()
                            .setPeekStatus(statusTypeOptional.get())
                            .setEndTimestamp()
            );
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean savePeekSessionCard(Long peekSessionId, Long cardId, Long userId) {
        try {
            Optional<PeekSession> peekSessionOptional = peekSessionRepository.findById(peekSessionId);
            if(peekSessionOptional.isEmpty())
                return false;

            if(!cardService.doesCardBelongToOwnerAndDeck(userId, peekSessionOptional.get().getDeck().getId(), cardId))
                return false;

            Optional<Card> cardOptional = cardRepository.getCardById(cardId);
            if(cardOptional.isEmpty())
                return false;

            peekSessionCardsRepository.save(new PeekSessionCards(cardOptional.get(), peekSessionOptional.get()));
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }
}
