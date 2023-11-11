package com.service.databaseservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "deck")
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "d_id")
    private Long id;

    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public Deck(Long id, String name, User owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    public Deck() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }
}