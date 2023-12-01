package com.service.databaseservice.model.cards;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.context.annotation.Lazy;

import java.sql.Blob;
import java.sql.Types;

@Entity
@Table(name = "text_answer_card")
public class TextAnswerCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id", insertable=false, updatable=false)
    private Long id;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Lob
    @JdbcTypeCode(Types.BINARY)
    @Lazy
    @Column(name = "image_data", columnDefinition = "bytea")
    private Blob imageData;

    public TextAnswerCard(Long id, String answer, Blob imageData) {
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

    public Blob getImageData() {
        return imageData;
    }
}