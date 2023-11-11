package com.service.databaseservice.model.cards;

import jakarta.persistence.*;

@Entity
@Table(name = "multiple_choice_card")
public class MultipleChoiceCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable=false, updatable=false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "c_id", nullable = false)
    private Card card;

    public MultipleChoiceCard(Long id, Card card) {
        this.id = id;
        this.card = card;
    }

    public MultipleChoiceCard() {
    }

    public Long getId() {
        return id;
    }

    public Card getCard() {
        return card;
    }
}