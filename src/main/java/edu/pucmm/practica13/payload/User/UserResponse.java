package edu.pucmm.practica13.payload.User;

public class UserResponse {
    private Long id;
    private String name;
    private String username;
    private String password;
    private String email;

    public Long getId() {
        return id;
    }

    public UserResponse setId(Long id) {
        this.id = id;

        return this;
    }

    public String getName() {
        return name;
    }

    public UserResponse setName(String name) {
        this.name = name;

        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserResponse setUsername(String username) {
        this.username = username;

        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserResponse setPassword(String password) {
        this.password = password;

        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserResponse setEmail(String email) {
        this.email = email;

        return this;
    }
}
