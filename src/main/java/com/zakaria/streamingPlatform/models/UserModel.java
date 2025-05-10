package com.zakaria.streamingPlatform.models;

import com.zakaria.streamingPlatform.entities.Role;

import java.time.LocalDate;

public class UserModel {
    private String username;
    private String email;
    private String password;
    private Role role;
    private boolean active;
    private LocalDate dateCreated;

    public UserModel() {
    }

    public UserModel(String username, String email, String password, Role role, boolean active, LocalDate dateCreated) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = active;
        this.dateCreated = dateCreated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
}

