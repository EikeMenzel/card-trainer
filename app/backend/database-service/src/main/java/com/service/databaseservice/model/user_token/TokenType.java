package com.service.databaseservice.model.user_token;

import jakarta.persistence.*;

@Entity
@Table(name = "token_type")
public class TokenType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tt_id")
    private Long id;

    @Column(name = "type", length = 64, nullable = false, unique = true)
    private String type;

    public TokenType() {
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
