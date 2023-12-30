package com.service.databaseservice.model.cards;

import com.service.databaseservice.model.Image;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.context.annotation.Lazy;

import java.sql.Blob;
import java.sql.Types;

@Entity
@Table(name = "text_answer_card")
public class TextAnswerCard {

    @Id
    @Column(name = "c_id", updatable = false)
    private Long id;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image imageData;

    public TextAnswerCard(Long id, String answer, Image imageData) {
        this.id = id;
        this.answer = answer;
        this.imageData = imageData;
    }

    public TextAnswerCard() {
    }

    public Long getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public Image getImageData() {
        return imageData;
    }
    public TextAnswerCard cloneTextAnswercard(Long cardId) {
        return new TextAnswerCard(cardId, this.answer, this.imageData);
    }

    public TextAnswerCard updateTextAnswerCard(String answer, Image image) {
        return new TextAnswerCard(this.id, answer, image);
    }

    public TextAnswerCard updateTextAnswerCard(String answer) {
        return new TextAnswerCard(this.id, answer, null);
    }
}