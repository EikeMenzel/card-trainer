package com.service.databaseservice.repository.sessions;

import com.service.databaseservice.model.sessions.LearnSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearnSessionRepository extends JpaRepository<LearnSession, Long> {
}
