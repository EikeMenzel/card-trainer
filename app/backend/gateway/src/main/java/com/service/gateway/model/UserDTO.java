package com.service.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("common-java:DuplicatedBlocks") // necessary on multiple services
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;

    public UserDTO() { /* This is for json parsing */ }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
