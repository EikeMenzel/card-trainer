package com.service.databaseservice.model;

import com.service.databaseservice.model.cards.Card;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "repetition")
public class Repetition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_id")
    private Long id;

    @Column(name = "quality", nullable = false)
    private Integer quality;

    @Column(name = "prev_ease_factor", nullable = false)
    private Float prevEaseFactor;

    @Column(name = "prev_interval", nullable = false)
    private Integer prevInterval;

    @Column(name = "next_learn_timestamp", nullable = false)
    private Timestamp nextLearnTimestamp;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
    public Repetition(Long id, Integer quality, Float prevEaseFactor, Integer prevInterval, User user, Card card) {
        this.id = id;
        this.quality = quality;
        this.prevEaseFactor = prevEaseFactor;
        this.prevInterval = prevInterval;
        this.user = user;
        this.card = card;
    }

    public Repetition() {
    }

    public Long getId() {
        return id;
    }

    public Integer getQuality() {
        return quality;
    }

    public Float getPrevEaseFactor() {
        return prevEaseFactor;
    }

    public Integer getPrevInterval() {
        return prevInterval;
    }

    public User getUser() {
        return user;
    }

    public Card getCard() {
        return card;
    }

    public Timestamp getNextLearnTimestamp() {
        return nextLearnTimestamp;
    }
}