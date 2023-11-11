package com.service.databaseservice.model.cards;

import jakarta.persistence.*;

@Entity
@Table(name = "text_answer_card")
public class TextAnswerCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable=false, updatable=false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "c_id", nullable = false)
    private Card card;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(name = "image_path")
    private String imagePath;

    public TextAnswerCard(Long id, Card card, String answer, String imagePath) {
        this.id = id;
        this.card = card;
        this.answer = answer;
        this.imagePath = imagePath;
    }

    public TextAnswerCard() {
    }

    public Long getId() {
        return id;
    }

    public Card getCard() {
        return card;
    }

    public String getAnswer() {
        return answer;
    }

    public String getImagePath() {
        return imagePath;
    }
}