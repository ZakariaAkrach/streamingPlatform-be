package com.zakaria.streamingPlatform.entities;

import com.zakaria.streamingPlatform.compositeKeys.UserMovieKey;
import jakarta.persistence.*;

@Entity
@Table(name = "user_movie_like")
public class UserMovieLikeEntity {
    @EmbeddedId
    private UserMovieKey userMovieKey;
    private Boolean liked;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;

    public UserMovieLikeEntity() {
    }

    public UserMovieLikeEntity(UserMovieKey userMovieKey, Boolean liked, UserEntity user, MovieEntity movie) {
        this.userMovieKey = userMovieKey;
        this.liked = liked;
        this.user = user;
        this.movie = movie;
    }

    public UserMovieKey getUserMovieKey() {
        return userMovieKey;
    }

    public void setUserMovieKey(UserMovieKey userMovieKey) {
        this.userMovieKey = userMovieKey;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public MovieEntity getMovie() {
        return movie;
    }

    public void setMovie(MovieEntity movie) {
        this.movie = movie;
    }
}
