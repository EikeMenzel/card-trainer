package com.service.databaseservice.model.cards;

import jakarta.persistence.*;

@Entity
@Table(name = "choice_answer")
public class ChoiceAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ca_id")
    private Long id;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "c_id", nullable = false)
    private MultipleChoiceCard multipleChoiceCard;

    public ChoiceAnswer(Long id, String answer, String imagePath, Boolean isCorrect, MultipleChoiceCard multipleChoiceCard) {
        this.id = id;
        this.answer = answer;
        this.imagePath = imagePath;
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

    public String getImagePath() {
        return imagePath;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public MultipleChoiceCard getMultipleChoiceCard() {
        return multipleChoiceCard;
    }
}