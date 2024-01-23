package com.service.databaseservice.payload.out;

import java.sql.Timestamp;

public record HistoryDetailDTO(Long historyId, String deckName, Timestamp createdAt, Timestamp finishedAt, int difficulty_1, int difficulty_2,  int difficulty_3, int difficulty_4, int difficulty_5, int difficulty_6, String status, int cardsLearned) {
}
