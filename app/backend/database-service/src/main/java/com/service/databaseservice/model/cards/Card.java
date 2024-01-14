package com.service.databaseservice.model.cards;

import com.service.databaseservice.model.Deck;
import com.service.databaseservice.model.Image;
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

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image imageData;

    @ManyToOne
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @ManyToOne
    @JoinColumn(name = "card_type_id", nullable = false)
    private CardType cardType;

    public Card(String question, Image imageData, Deck deck, CardType cardType) {
        this.question = question;
        this.imageData = imageData;
        this.deck = deck;
        this.cardType = cardType;
    }

    public Card(Long id, String question, Image imageData, Deck deck, CardType cardType) {
        this.id = id;
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

    public Image getImageData() {
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

    public Card updateCard(String question, Image newImage) {
        return new Card(this.id, question, newImage, this.deck, this.cardType);
    }
    public Card updateCard(String question) {
        return new Card(this.id, question, null, this.deck, this.cardType);
    }

}