package com.service.databaseservice.model.cards;

import com.service.databaseservice.model.Deck;
import jakarta.persistence.*;

@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Long id;

    @Column(name = "question", columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(name = "image_path")
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @ManyToOne
    @JoinColumn(name = "card_type_id", nullable = false)
    private CardType cardType;

    public Card(Long id, String question, String imagePath, Deck deck, CardType cardType) {
        this.id = id;
        this.question = question;
        this.imagePath = imagePath;
        this.deck = deck;
        this.cardType = cardType;
    }

    public Card() {
    }

    public Long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Deck getDeck() {
        return deck;
    }

    public CardType getCardType() {
        return cardType;
    }
}