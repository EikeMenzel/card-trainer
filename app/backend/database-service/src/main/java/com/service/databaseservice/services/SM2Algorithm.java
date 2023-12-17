package com.service.databaseservice.services;

import com.service.databaseservice.model.Repetition;
import com.service.databaseservice.payload.inc.learnsession.RatingLevelDTO;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class SM2Algorithm {
    public static Repetition calc(Repetition repetition, RatingLevelDTO ratingLevelDTO) {
        int interval;
        double easeFactor = repetition.getPrevEaseFactor();
        int repetitions = repetition.getRepetition();
        int quality = getQualityFromDifficultyLevel(ratingLevelDTO);

        if (quality >= 3) {
            interval = switch (repetitions) {
                case 0 -> 1;
                case 1 -> 6;
                default -> (int) Math.round(repetition.getPrevInterval() * easeFactor);
            };
            repetitions = repetitions + 1;
            easeFactor = repetition.getPrevEaseFactor() + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));

        } else {
            repetitions = 0;
            interval = 1;
        }

        if (easeFactor < 1.3) {
            easeFactor = 1.3;
        }

        var nextLearnTimestamp = Timestamp.valueOf(LocalDateTime.now().plusDays(interval));

        repetition.setRepetition(repetitions);
        repetition.setPrevEaseFactor(easeFactor);
        repetition.setPrevInterval(interval);
        repetition.setNextLearnTimestamp(nextLearnTimestamp);
        repetition.setQuality(quality);

        return repetition;
    }

    private static int getQualityFromDifficultyLevel(RatingLevelDTO ratingLevelDTO) {
        return ratingLevelDTO.ordinal();
    }
    private SM2Algorithm() { }
}
