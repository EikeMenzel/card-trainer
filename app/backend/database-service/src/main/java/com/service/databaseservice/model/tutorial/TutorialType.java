package com.service.databaseservice.model.tutorial;

import jakarta.persistence.*;

@Entity
@Table(name = "tutorial_page_type")
public class TutorialType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tpt_id")
    private Long id;

    @Column(name = "type", length = 32, nullable = false, unique = true)
    private String type;

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}