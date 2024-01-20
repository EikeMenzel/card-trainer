package com.service.databaseservice.model.tutorial;

import com.service.databaseservice.model.User;
import jakarta.persistence.*;

@Entity
@Table(name = "user_tutorial_page")
public class UserTutorialPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "utp_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tutorial_page_type_id")
    private TutorialType tutorialType;

    public UserTutorialPage() {
    }

    public UserTutorialPage(User user, TutorialType tutorialType) {
        this.user = user;
        this.tutorialType = tutorialType;
    }
}