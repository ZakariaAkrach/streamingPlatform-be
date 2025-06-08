package com.zakaria.streamingPlatform.dto;

public class UserMovieFavoriteDTO {
    private Long movieId;
    private boolean favorite;
    private UserPublicDTO user;

    public UserMovieFavoriteDTO() {
    }

    public UserMovieFavoriteDTO(Long movieId, UserPublicDTO user, boolean favorite) {
        this.movieId = movieId;
        this.user = user;
        this.favorite = favorite;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public UserPublicDTO getUser() {
        return user;
    }

    public void setUser(UserPublicDTO user) {
        this.user = user;
    }
}
