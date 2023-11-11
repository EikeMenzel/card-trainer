package com.service.databaseservice.model;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "u_id")
    private Long id;

    @Column(name = "username", length = 30, nullable = false)
    private String username;

    @Column(name = "email", length = 64, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 60, nullable = false)
    private String password;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @Column(name = "gets_notified", nullable = false)
    private Boolean getsNotified = false;

    @Column(name = "cards_per_session", nullable = false)
    private Integer cardsPerSession = 20;

    @Column(name = "lang_code", length = 3, nullable = false)
    private String langCode;

    public User(Long id, String username, String email, String password, Boolean isVerified, Boolean getsNotified, Integer cardsPerSession, String langCode) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isVerified = isVerified;
        this.getsNotified = getsNotified;
        this.cardsPerSession = cardsPerSession;
        this.langCode = langCode;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public Boolean getGetsNotified() {
        return getsNotified;
    }

    public Integer getCardsPerSession() {
        return cardsPerSession;
    }

    public String getLangCode() {
        return langCode;
    }
}
