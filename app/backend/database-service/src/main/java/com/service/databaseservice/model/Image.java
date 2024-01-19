package com.service.databaseservice.model;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.context.annotation.Lazy;

import java.sql.Blob;
import java.sql.Types;

@Entity
@Table(name = "image")
@DynamicUpdate
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i_id")
    private Long id;

    @Lob
    @JdbcTypeCode(Types.BINARY)
    @Lazy
    @Column(name = "image_data", columnDefinition = "bytea", nullable = false)
    private Blob data;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Image() {
    }

    public Image(Long id, Blob data, User user) {
        this.id = id;
        this.data = data;
        this.user = user;
    }

    public Image(Blob data, User user) {
        this.data = data;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Blob getData() {
        return data;
    }

    public User getUser() {
        return user;
    }

    public void setData(Blob imageData) {
        this.data = imageData;
    }

    public Image updateImage(Blob data) {
        setData(data);
        return this;
    }

    public Image cloneImage(User newUser) {
        return new Image(this.data, newUser);
    }
}
