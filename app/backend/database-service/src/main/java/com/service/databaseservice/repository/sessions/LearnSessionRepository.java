package com.service.databaseservice.repository.sessions;

import com.service.databaseservice.model.sessions.LearnSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LearnSessionRepository extends JpaRepository<LearnSession, Long> {
    Optional<LearnSession> findTopByDeckIdAndUserIdOrderByIdDesc(Long deckId, Long userId);
    List<LearnSession> getAllByUserIdAndDeckId(Long userId, Long deckId);
    List<LearnSession> getAllByUserId(Long userId);
    Optional<LearnSession> getLearnSessionByIdAndUserIdAndDeckId(Long sessionId, Long userId, Long deckId);
    Boolean existsLearnSessionByIdAndUserId(Long sessionId, Long userId);
    @Query("SELECT CASE WHEN COUNT(ls) > 0 THEN TRUE ELSE FALSE END FROM LearnSession ls WHERE ls.user.id = :userId AND ls.status.type = 'FINISHED' AND DATE(ls.finishedAt) = CURRENT_DATE")
    Boolean isLearnSessionCompletedToday(Long userId);

    @Query("SELECT ls FROM LearnSession ls WHERE ls.user.id = :userId AND ls.status.type = 'FINISHED' AND DATE(ls.finishedAt) = CURRENT_DATE")
    List<LearnSession> findLearnSessionsFinishedTodayByUserId(@Param("userId") Long userId);}
