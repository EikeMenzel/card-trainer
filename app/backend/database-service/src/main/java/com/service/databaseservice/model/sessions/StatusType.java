package com.service.databaseservice.model.sessions;

import jakarta.persistence.*;

@Entity
@Table(name = "status_type")
public class StatusType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "st_id")
    private Long id;

    @Column(name = "type", length = 64, nullable = false, unique = true)
    private String type;

    public StatusType() {
        /* NoArgsConstructor for Hibernate */
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}