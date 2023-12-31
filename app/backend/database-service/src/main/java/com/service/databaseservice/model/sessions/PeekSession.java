package com.service.databaseservice.model.sessions;

import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.User;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "peek_session")
public class PeekSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ps_id")
    private Long id;

    @Column(name = "created_at", nullable = false)
    private final Timestamp createdAt = Timestamp.from(Instant.now());

    @Column(name = "finished_at")
    private Timestamp finishedAt;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private StatusType status;

    @ManyToOne
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public PeekSession(User user, Deck deck, StatusType status) {
        this.user = user;
        this.deck = deck;
        this.status = status;
    }

    public PeekSession() {
    }

    public Long getId() {
        return id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getFinishedAt() {
        return finishedAt;
    }

    public StatusType getStatus() {
        return status;
    }

    public Deck getDeck() {
        return deck;
    }

    public User getUser() {
        return user;
    }

    public PeekSession setPeekStatus(StatusType status) {
        this.status = status;
        return this;
    }

    public PeekSession setEndTimestamp() {
        this.finishedAt = Timestamp.from(Instant.now());
        return this;
    }
}