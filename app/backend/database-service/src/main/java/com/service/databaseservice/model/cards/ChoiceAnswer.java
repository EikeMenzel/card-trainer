package com.service.databaseservice.model.cards;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.context.annotation.Lazy;

import java.sql.Blob;
import java.sql.Types;

@Entity
@Table(name = "choice_answer")
public class ChoiceAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ca_id")
    private Long id;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Lob
    @JdbcTypeCode(Types.BINARY)
    @Lazy
    @Column(name = "image_data", columnDefinition = "bytea")
    private Blob imageData;
    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "c_id", nullable = false)
    private MultipleChoiceCard multipleChoiceCard;

    public ChoiceAnswer(String answer, Blob imageData, Boolean isCorrect, MultipleChoiceCard multipleChoiceCard) {
        this.answer = answer;
        this.imageData = imageData;
        this.isCorrect = isCorrect;
        this.multipleChoiceCard = multipleChoiceCard;
    }

    public ChoiceAnswer() {
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

    public Boolean getCorrect() {
        return isCorrect;
    }

    public MultipleChoiceCard getMultipleChoiceCard() {
        return multipleChoiceCard;
    }
}