package com.service.databaseservice.repository.sessions;

import com.service.databaseservice.model.sessions.PeekSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface PeekSessionRepository extends JpaRepository<PeekSession, Long> {
    Optional<PeekSession> getPeekSessionByIdAndUserId(Long sessionId, Long userId);

    @Query("SELECT ps FROM PeekSession ps WHERE ps.status.type != 'FINISHED' AND ps.createdAt < :timestamp")
    List<PeekSession> findByStatusNotAndCreatedAtBefore(Timestamp timestamp);

}
