package com.service.databaseservice.repository.sessions;

import com.service.databaseservice.model.sessions.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusTypeRepository extends JpaRepository<StatusType, Long> {
}
