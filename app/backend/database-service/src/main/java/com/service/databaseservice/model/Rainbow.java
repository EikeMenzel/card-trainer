package com.service.databaseservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rainbow")
public class Rainbow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_id")
    private Long id;

    @Column(name = "password", nullable = false, unique = true)
    private String password;

    public Rainbow() {
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}