package com.service.databaseservice.model.cards;

import com.service.databaseservice.model.Image;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.context.annotation.Lazy;

import java.awt.*;
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

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image imageData;
    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "c_id", nullable = false)
    private MultipleChoiceCard multipleChoiceCard;

    public ChoiceAnswer(String answer, Image imageData, Boolean isCorrect, MultipleChoiceCard multipleChoiceCard) {
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

    public Image getImageData() {
        return imageData;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public MultipleChoiceCard getMultipleChoiceCard() {
        return multipleChoiceCard;
    }
    public ChoiceAnswer cloneWithDifferentMultipleChoiceCard(MultipleChoiceCard newMultipleChoiceCard) {
        return new ChoiceAnswer(this.answer, this.imageData, this.isCorrect, newMultipleChoiceCard);
    }
}