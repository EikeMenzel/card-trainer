package com.service.databaseservice.model.cards;

import jakarta.persistence.*;

@Entity
@Table(name = "card_type")
public class CardType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ct_id")
    private Long id;

    @Column(name = "type", length = 64, nullable = false, unique = true)
    private String type;

    public CardType() {
        /* NoArgsConstructor for Hibernate */
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}