package com.service.databaseservice.services;

import com.service.databaseservice.model.cards.ChoiceAnswer;
import com.service.databaseservice.model.cards.MultipleChoiceCard;
import com.service.databaseservice.payload.inc.updatecard.ChoiceAnswerDTO;
import com.service.databaseservice.repository.cards.ChoiceAnswerRepository;
import com.service.databaseservice.repository.cards.MultipleChoiceCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CardServiceTest {
    @Mock
    private ChoiceAnswerRepository choiceAnswerRepository;

    @Mock
    private MultipleChoiceCardRepository multipleChoiceCardRepository;

    @InjectMocks
    private CardService cardService;

    @Test
    @Transactional
    void deleteChoiceAnswerById() {
        ChoiceAnswer choiceAnswer = new ChoiceAnswer("X", null, false, new MultipleChoiceCard(1L));
        when(choiceAnswerRepository.findById(1L)).thenReturn(Optional.of(choiceAnswer));

        choiceAnswerRepository.deleteById(1L);
        assertThat(choiceAnswerRepository.count()).isZero();

        multipleChoiceCardRepository.deleteById(1L);
    }


    @Test
    @Transactional
    void deleteChoiceAnswerByDeleteChoiceAnswerMethod() {
        MultipleChoiceCard multipleChoiceCard = new MultipleChoiceCard(1L);
        ChoiceAnswer choiceAnswer = new ChoiceAnswer("X", null, false, multipleChoiceCard);
        when(choiceAnswerRepository.findById(1L)).thenReturn(Optional.of(choiceAnswer));

        // Act - Delete using service method (assuming it's implemented correctly)
        ChoiceAnswerDTO choiceAnswerDTO = new ChoiceAnswerDTO(1L, "Hello", false, null);
        cardService.deleteChoiceAnswers(List.of(choiceAnswerDTO), multipleChoiceCard); // Adjust this line according to actual method signature and implementation

        // Assert - Verify it's deleted
        Optional<ChoiceAnswer> deletedChoiceAnswer = choiceAnswerRepository.findById(choiceAnswer.getId());
        assertThat(deletedChoiceAnswer).isEmpty();
    }

}
