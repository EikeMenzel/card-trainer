package com.service.databaseservice.services;

import com.service.databaseservice.model.Repetition;
import com.service.databaseservice.payload.inc.learnsession.RatingLevelDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SM2AlgorithmTest {
    @Test
    void testCalc_FirstRepetition() {
        Repetition repetition = new Repetition();
        repetition.setRepetition(0);
        repetition.setPrevEaseFactor(2.5);
        repetition.setPrevInterval(0);
        repetition.setQuality(-1);

        RatingLevelDTO ratingLevelDTO = RatingLevelDTO.RATING_5;

        Repetition updatedRepetition = SM2Algorithm.calc(repetition, ratingLevelDTO);

        assertEquals(1, updatedRepetition.getRepetition());
        assertEquals(1, updatedRepetition.getPrevInterval());
    }

    @Test
    void testCalc_SecondRepetition() {
        Repetition repetition = new Repetition();
        repetition.setRepetition(1);
        repetition.setPrevEaseFactor(2.5);
        repetition.setPrevInterval(1);

        RatingLevelDTO ratingLevelDTO = RatingLevelDTO.RATING_3;

        // Execute
        Repetition updatedRepetition = SM2Algorithm.calc(repetition, ratingLevelDTO);

        // Verify
        assertEquals(2, updatedRepetition.getRepetition());
        assertEquals(6, updatedRepetition.getPrevInterval());
    }

    @Test
    void testCalc_SubsequentRepetitions() {
        Repetition repetition = new Repetition();
        repetition.setRepetition(2);
        repetition.setPrevEaseFactor(2.5);
        repetition.setPrevInterval(6);

        RatingLevelDTO ratingLevelDTO = RatingLevelDTO.RATING_3;

        Repetition updatedRepetition = SM2Algorithm.calc(repetition, ratingLevelDTO);

        assertEquals(3, updatedRepetition.getRepetition());
        assertEquals(2.36, updatedRepetition.getPrevEaseFactor());
    }
}