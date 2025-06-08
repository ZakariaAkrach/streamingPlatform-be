package com.zakaria.streamingPlatform.compositeKeys;

import jakarta.persistence.Embeddable;

@Embeddable
public class UserMovieKey {
    private Long userId;
    private Long movieId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
}
