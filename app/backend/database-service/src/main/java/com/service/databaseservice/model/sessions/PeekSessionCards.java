package com.service.databaseservice.model.sessions;

import com.service.databaseservice.model.cards.Card;
import jakarta.persistence.*;

@Entity
@Table(name = "peek_session_cards")
public class PeekSessionCards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "psc_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne
    @JoinColumn(name = "peek_session_id", nullable = false)
    private PeekSession peekSession;

    public PeekSessionCards(Card card, PeekSession peekSession) {
        this.card = card;
        this.peekSession = peekSession;
    }

    public PeekSessionCards() {
    }

    public Long getId() {
        return id;
    }

    public Card getCard() {
        return card;
    }

    public PeekSession getPeekSession() {
        return peekSession;
    }
}