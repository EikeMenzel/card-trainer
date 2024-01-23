package com.service.databaseservice.model;

import com.service.databaseservice.model.cards.Card;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "repetition")
public class RepetitionModel { // renamed because of java:S1700

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_id")
    private Long id;

    @Column(name = "repetition", nullable = false)
    private Integer repetition;

    @Column(name = "quality", nullable = false)
    private Integer quality = -1;

    @Column(name = "prev_ease_factor", nullable = false)
    private double prevEaseFactor;

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
    public RepetitionModel(Integer repetition, Integer quality, double prevEaseFactor, Integer prevInterval, Timestamp nextLearnTimestamp, User user, Card card) {
        this.repetition = repetition;
        this.quality = quality;
        this.prevEaseFactor = prevEaseFactor;
        this.prevInterval = prevInterval;
        this.nextLearnTimestamp = nextLearnTimestamp;
        this.user = user;
        this.card = card;
    }

    public RepetitionModel() {
    }

    public Long getId() {
        return id;
    }

    public Integer getQuality() {
        return quality;
    }

    public double getPrevEaseFactor() {
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

    public Integer getRepetition() {
        return repetition;
    }

    public void setRepetition(Integer repetition) {
        this.repetition = repetition;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public void setPrevEaseFactor(double prevEaseFactor) {
        this.prevEaseFactor = prevEaseFactor;
    }

    public void setPrevInterval(Integer prevInterval) {
        this.prevInterval = prevInterval;
    }

    public void setNextLearnTimestamp(Timestamp nextLearnTimestamp) {
        this.nextLearnTimestamp = nextLearnTimestamp;
    }
}