package com.zakaria.streamingPlatform.dto;

public class UserMovieLikeDTO {
    private Long movieId;
    private Boolean liked;
    public UserPublicDTO user;

    public UserMovieLikeDTO() {
    }

    public UserMovieLikeDTO(Long movieId, Boolean liked, UserPublicDTO user) {
        this.movieId = movieId;
        this.liked = liked;
        this.user = user;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public UserPublicDTO getUser() {
        return user;
    }

    public void setUser(UserPublicDTO user) {
        this.user = user;
    }
}
