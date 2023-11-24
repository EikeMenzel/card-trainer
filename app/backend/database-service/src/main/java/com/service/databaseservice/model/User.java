package com.service.databaseservice.model;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "users")
@DynamicUpdate
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
    private String langCode = "EN";

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

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
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

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGetsNotified(Boolean getsNotified) {
        this.getsNotified = getsNotified;
    }

    public void setCardsPerSession(Integer cardsPerSession) {
        this.cardsPerSession = cardsPerSession;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }
}
