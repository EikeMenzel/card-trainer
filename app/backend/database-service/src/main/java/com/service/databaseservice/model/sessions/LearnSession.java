package com.service.databaseservice.model.sessions;

import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.User;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "learn_session")
public class LearnSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ls_id")
    private Long id;

    @Column(name = "created_at", nullable = false)
    private final Timestamp createdAt = Timestamp.from(Instant.now());

    @Column(name = "finished_at")
    private Timestamp finishedAt;

    @Column(name = "rating_1", nullable = false)
    private Integer rating1 = 0;
    @Column(name = "rating_2", nullable = false)
    private Integer rating2 = 0;
    @Column(name = "rating_3", nullable = false)
    private Integer rating3 = 0;
    @Column(name = "rating_4", nullable = false)
    private Integer rating4 = 0;
    @Column(name = "rating_5", nullable = false)
    private Integer rating5 = 0;
    @Column(name = "rating_6", nullable = false)
    private Integer rating6 = 0;
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private StatusType status;

    @ManyToOne
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public LearnSession(User user, Deck deck, StatusType status) {
        this.user = user;
        this.deck = deck;
        this.status = status;
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

    public Integer getRating1() {
        return rating1;
    }

    public Integer getRating2() {
        return rating2;
    }

    public Integer getRating3() {
        return rating3;
    }

    public Integer getRating4() {
        return rating4;
    }

    public Integer getRating5() {
        return rating5;
    }

    public Integer getRating6() {
        return rating6;
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

    public void setRating1(Integer rating1) {
        this.rating1 = rating1;
    }

    public void setRating2(Integer rating2) {
        this.rating2 = rating2;
    }

    public void setRating3(Integer rating3) {
        this.rating3 = rating3;
    }

    public void setRating4(Integer rating4) {
        this.rating4 = rating4;
    }

    public void setRating5(Integer rating5) {
        this.rating5 = rating5;
    }

    public void setRating6(Integer rating6) {
        this.rating6 = rating6;
    }

    public void setFinishedAt(Timestamp finishedAt) {
        this.finishedAt = finishedAt;
    }

    public LearnSession setLearnStatus(StatusType status) {
        this.status = status;
        return this;
    }

    public LearnSession setEndTimestamp() {
        this.finishedAt = Timestamp.from(Instant.now());
        return this;
    }
}