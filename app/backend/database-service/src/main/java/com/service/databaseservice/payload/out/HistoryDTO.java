package com.service.databaseservice.payload.out;

import java.sql.Timestamp;

public record HistoryDTO(Long historyId, Timestamp createdAt, String status, int cardsLearned) {
}
