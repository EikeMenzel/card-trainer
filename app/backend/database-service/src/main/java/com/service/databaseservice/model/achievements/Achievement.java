package com.service.databaseservice.model.achievements;

import jakarta.persistence.*;

@Entity
@Table(name = "achievement")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a_id")
    private Long id;

    @Column(name = "name", length = 128, nullable = false, unique = true)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "is_daily", columnDefinition = "BOOLEAN", nullable = false)
    private Boolean isDaily;

    public Achievement() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getDaily() {
        return isDaily;
    }
}