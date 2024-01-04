package com.service.databaseservice.model.achievements;

import com.service.databaseservice.model.User;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "user_login_tracker")
public class UserLoginTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ult_id")
    private Long id;

    @Column(name = "date", nullable = false)
    private final Timestamp date = Timestamp.from(Instant.now());

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserLoginTracker(User user) {
        this.user = user;
    }
    public UserLoginTracker() {
    }

    public Long getId() {
        return id;
    }

    public Timestamp getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }
}
