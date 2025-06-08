package com.zakaria.streamingPlatform.entities;

import com.zakaria.streamingPlatform.compositeKeys.UserMovieKey;
import jakarta.persistence.*;

@Entity
@Table(name = "user_movie_favorite")
public class UserMovieFavoriteEntity {
    @EmbeddedId
    private UserMovieKey userMovieKey;
    private boolean favorite;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public UserMovieFavoriteEntity() {
    }

    public UserMovieFavoriteEntity(UserMovieKey userMovieKey, boolean favorite, MovieEntity movie, UserEntity user) {
        this.userMovieKey = userMovieKey;
        this.favorite = favorite;
        this.movie = movie;
        this.user = user;
    }

    public UserMovieKey getUserMovieKey() {
        return userMovieKey;
    }

    public void setUserMovieKey(UserMovieKey userMovieKey) {
        this.userMovieKey = userMovieKey;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public MovieEntity getMovie() {
        return movie;
    }

    public void setMovie(MovieEntity movie) {
        this.movie = movie;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
