package com.service.authenticationservice.payload.inc;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private boolean isVerify;

    public UserDTO(Long id, String username, String email, String password, boolean isVerify) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isVerify = isVerify;
    }

    public UserDTO(String username, String email, String password, boolean isVerify) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isVerify = isVerify;
    }

    public UserDTO() {
    }

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

    public boolean isVerify() {
        return isVerify;
    }
}
