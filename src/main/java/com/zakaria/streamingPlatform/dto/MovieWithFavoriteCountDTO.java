package com.zakaria.streamingPlatform.dto;

public class MovieWithFavoriteCountDTO {
    private Long id;
    private String title;
    private Long favoriteCount;

    public MovieWithFavoriteCountDTO() {
    }

    public MovieWithFavoriteCountDTO(Long id, String title, Long favoriteCount) {
        this.id = id;
        this.title = title;
        this.favoriteCount = favoriteCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }
}
