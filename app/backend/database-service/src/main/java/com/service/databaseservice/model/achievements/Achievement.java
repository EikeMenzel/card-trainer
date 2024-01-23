package com.service.databaseservice.model.achievements;

import com.service.databaseservice.model.Image;
import jakarta.persistence.*;

@Entity
@Table(name = "achievement")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a_id")
    private Long id;

    @Column(name = "name", length = 128, nullable = false, unique = true)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "is_daily", columnDefinition = "BOOLEAN", nullable = false)
    private Boolean isDaily;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image imageData;
    public Achievement() {
        /* NoArgsConstructor for Hibernate */
    }

    public Achievement(Long id, String name, String description, Boolean isDaily, Image imageData) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isDaily = isDaily;
        this.imageData = imageData;
    }

    public Achievement(String name, String description, Boolean isDaily, Image imageData) {
        this.name = name;
        this.description = description;
        this.isDaily = isDaily;
        this.imageData = imageData;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getDaily() {
        return isDaily;
    }

    public Image getImageData() {
        return imageData;
    }
}