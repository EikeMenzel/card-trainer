package com.service.databaseservice.model.achievements;

import com.service.databaseservice.model.User;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "user_achievement")
public class UserAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ua_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;

    @Column(name = "achieved_at", nullable = false)
    private Timestamp achievedAt;

    public UserAchievement(Long id, User user, Achievement achievement, Timestamp achievedAt) {
        this.id = id;
        this.user = user;
        this.achievement = achievement;
        this.achievedAt = achievedAt;
    }

    public UserAchievement() {
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public Timestamp getAchievedAt() {
        return achievedAt;
    }
}