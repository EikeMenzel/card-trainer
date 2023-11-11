package com.service.databaseservice.model.achievements;

import com.service.databaseservice.model.User;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "user_login_tracker")
public class UserLoginTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ult_id")
    private Long id;

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserLoginTracker(Long id, Timestamp date, User user) {
        this.id = id;
        this.date = date;
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
