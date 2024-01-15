package com.service.databaseservice.services;

import com.service.databaseservice.model.Repetition;
import com.service.databaseservice.model.User;
import com.service.databaseservice.model.cards.Card;
import com.service.databaseservice.payload.inc.learnsession.RatingLevelDTO;
import com.service.databaseservice.repository.RepetitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepetitionService.class)
class RepetitionServiceTest {

    @MockBean
    private RepetitionRepository repetitionRepository;

    @Autowired
    private RepetitionService repetitionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRepetitionByCardId_ExistingCard() {
        Long cardId = 1L;
        Repetition repetition = new Repetition();
        when(repetitionRepository.getRepetitionByCard_Id(cardId)).thenReturn(Optional.of(repetition));

        Optional<Repetition> result = repetitionService.getRepetitionByCardId(cardId);

        assertTrue(result.isPresent());
        assertEquals(repetition, result.get());
    }

    @Test
    void getRepetitionByCardId_NonExistingCard() {
        Long cardId = 1L;
        when(repetitionRepository.getRepetitionByCard_Id(cardId)).thenReturn(Optional.empty());

        Optional<Repetition> result = repetitionService.getRepetitionByCardId(cardId);

        assertFalse(result.isPresent());
    }

    @Test
    void initRepetition_WithValidInputs() {
        Card card = new Card();
        User user = new User();

        assertTrue(repetitionService.initRepetition(card, user));
        verify(repetitionRepository).save(any(Repetition.class));
    }

    @Test
    void initRepetition_WithNullInputs() {
        assertFalse(repetitionService.initRepetition(null, null));
        verify(repetitionRepository, never()).save(any());
    }

    @Test
    void updateRepetition_NonExistingCard() {
        Long cardId = 1L;
        RatingLevelDTO ratingLevelDTO = RatingLevelDTO.RATING_5;
        when(repetitionRepository.getRepetitionByCard_Id(cardId)).thenReturn(Optional.empty());

        assertFalse(repetitionService.updateRepetition(cardId, ratingLevelDTO));
        verify(repetitionRepository, never()).save(any(Repetition.class));
    }
}
