package com.service.cardsservice.payload.in;

import java.sql.Timestamp;

public record HistoryDTO(Long historyId, Timestamp createdAt, String status, int cardsLearned) {
}
