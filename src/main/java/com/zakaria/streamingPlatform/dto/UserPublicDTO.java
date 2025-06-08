package com.zakaria.streamingPlatform.dto;

public class UserPublicDTO {
    private String username;

    public UserPublicDTO() {
    }

    public UserPublicDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
