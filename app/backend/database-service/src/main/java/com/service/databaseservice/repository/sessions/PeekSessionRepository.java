package com.service.databaseservice.repository.sessions;

import com.service.databaseservice.model.sessions.PeekSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PeekSessionRepository extends JpaRepository<PeekSession, Long> {
    Optional<PeekSession> getPeekSessionByIdAndUserId(Long sessionId, Long userId);

}
