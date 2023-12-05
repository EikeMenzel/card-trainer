package com.service.databaseservice.model.cards;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "multiple_choice_card")
public class MultipleChoiceCard {

    @Id
    @Column(name = "c_id", updatable=false)
    private Long id;

    @OneToMany(mappedBy = "multipleChoiceCard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChoiceAnswer> choiceAnswerList;

    public MultipleChoiceCard(Long id, List<ChoiceAnswer> choiceAnswerList) {
        this.id = id;
        this.choiceAnswerList = choiceAnswerList;
    }

    public MultipleChoiceCard(Long id) {
        this.id = id;
    }
    public MultipleChoiceCard() {
    }

    public Long getId() {
        return id;
    }

    public List<ChoiceAnswer> getChoiceAnswerList() {
        return choiceAnswerList;
    }
}