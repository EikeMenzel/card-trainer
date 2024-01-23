package com.service.authenticationservice.payload.inc;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
@SuppressWarnings("common-java:DuplicatedBlocks") // necessary on multiple services
public class UserDTO {
    @Min(value = 0) private Long id;
    @Size(min = 4, max = 30) private String username;
    @Size(min = 6, max = 30) private String email;
    @Size(min = 8, max = 72) private String password;
    private boolean isVerify;

    public UserDTO(@Min(value = 0) Long id, @Size(min = 4, max = 30) String username, @Size(min = 6, max = 64) String email, @Size(min = 8, max = 72) String password, boolean isVerify) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isVerify = isVerify;
    }

    public UserDTO(@Size(min = 4, max = 30) String username, @Size(min = 6, max = 64) String email, @Size(min = 8, max = 72) String password, boolean isVerify) {
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
