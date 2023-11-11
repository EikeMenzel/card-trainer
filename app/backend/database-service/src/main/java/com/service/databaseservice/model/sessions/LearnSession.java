package com.service.databaseservice.model.sessions;

import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.User;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "learn_session")
public class LearnSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ls_id")
    private Long id;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "finished_at")
    private Timestamp finishedAt;

    @Column(name = "difficulty_1", nullable = false)
    private Integer difficulty1 = 0;
    @Column(name = "difficulty_2", nullable = false)
    private Integer difficulty2 = 0;
    @Column(name = "difficulty_3", nullable = false)
    private Integer difficulty3 = 0;
    @Column(name = "difficulty_4", nullable = false)
    private Integer difficulty4 = 0;
    @Column(name = "difficulty_5", nullable = false)
    private Integer difficulty5 = 0;
    @Column(name = "difficulty_6", nullable = false)
    private Integer difficulty6 = 0;
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private StatusType status;

    @ManyToOne
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public LearnSession(Long id, Timestamp createdAt, Timestamp finishedAt, Integer difficulty1, Integer difficulty2, Integer difficulty3, Integer difficulty4, Integer difficulty5, Integer difficulty6, StatusType status, Deck deck, User user) {
        this.id = id;
        this.createdAt = createdAt;
        this.finishedAt = finishedAt;
        this.difficulty1 = difficulty1;
        this.difficulty2 = difficulty2;
        this.difficulty3 = difficulty3;
        this.difficulty4 = difficulty4;
        this.difficulty5 = difficulty5;
        this.difficulty6 = difficulty6;
        this.status = status;
        this.deck = deck;
        this.user = user;
    }

    public LearnSession() {
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

    public Integer getDifficulty1() {
        return difficulty1;
    }

    public Integer getDifficulty2() {
        return difficulty2;
    }

    public Integer getDifficulty3() {
        return difficulty3;
    }

    public Integer getDifficulty4() {
        return difficulty4;
    }

    public Integer getDifficulty5() {
        return difficulty5;
    }

    public Integer getDifficulty6() {
        return difficulty6;
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
}