package com.zakaria.streamingPlatform.response;

import com.zakaria.streamingPlatform.entities.Role;

public class ResponseToken {
    private int status;
    private String message;
    private String token;
    private Role role;

    public ResponseToken() {
    }

    public ResponseToken(int status, String message, String token, Role role) {
        this.status = status;
        this.message = message;
        this.token = token;
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
