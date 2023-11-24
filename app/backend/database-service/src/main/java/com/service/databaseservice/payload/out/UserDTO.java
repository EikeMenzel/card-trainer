package com.service.databaseservice.payload.out;

public class UserDTO {
    private boolean isVerify;
    private Long id;
    private String username;
    private String email;
    private String password;

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

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVerify() {
        return isVerify;
    }

    public void setVerify(boolean verify) {
        isVerify = verify;
    }
}
