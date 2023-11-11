package com.service.databaseservice.model.user_token;

import com.service.databaseservice.model.User;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_token")
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ut_id")
    private Long id;

    @Column(name = "token_value", length = 50, nullable = false, unique = true)
    private String tokenValue;

    @Column(name = "expiry_timestamp", nullable = false)
    private Timestamp expiryTimestamp;

    @ManyToOne
    @JoinColumn(name = "token_type", nullable = false)
    private TokenType tokenType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserToken(Long id, String tokenValue, Timestamp expiryTimestamp, TokenType tokenType, User user) {
        this.id = id;
        this.tokenValue = tokenValue;
        this.expiryTimestamp = expiryTimestamp;
        this.tokenType = tokenType;
        this.user = user;
    }

    public UserToken() {
    }

    public Long getId() {
        return id;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public Timestamp getExpiryTimestamp() {
        return expiryTimestamp;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public User getUser() {
        return user;
    }
}
