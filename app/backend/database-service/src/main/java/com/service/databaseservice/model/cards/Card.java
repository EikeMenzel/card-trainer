package com.service.databaseservice.model.cards;

import com.service.databaseservice.model.Deck;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.springframework.context.annotation.Lazy;

import java.sql.Blob;
import java.sql.Types;

@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Long id;

    @Column(name = "question", columnDefinition = "TEXT", nullable = false)
    private String question;

    @Lob
    @JdbcTypeCode(Types.BINARY)
    @Lazy
    @Column(name = "image_data", columnDefinition = "bytea")
    private Blob imageData;

    @ManyToOne
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @ManyToOne
    @JoinColumn(name = "card_type_id", nullable = false)
    private CardType cardType;

    public Card(String question, Blob imageData, Deck deck, CardType cardType) {
        this.question = question;
        this.imageData = imageData;
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

    public Blob getImageData() {
        return imageData;
    }

    public Deck getDeck() {
        return deck;
    }

    public CardType getCardType() {
        return cardType;
    }

    public Card cloneWithDifferentDeck(Deck newDeck) {
        return new Card(this.question, this.imageData, newDeck, this.cardType);
    }
}