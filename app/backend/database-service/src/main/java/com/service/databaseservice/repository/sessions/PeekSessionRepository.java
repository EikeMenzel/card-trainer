package com.service.databaseservice.repository.sessions;

import com.service.databaseservice.model.sessions.PeekSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeekSessionRepository extends JpaRepository<PeekSession, Long> {
}
