package com.zakaria.streamingPlatform.dto;

import com.zakaria.streamingPlatform.entities.TypeMovie;

import java.time.LocalDate;
import java.util.List;

public class MovieDTO {
    private int idTheMovieDb;
    private TypeMovie typeMovie;
    private String language;
    private String title;
    private String description;
    private String posterPath; //img small
    private String backdropPath; //img big
    private LocalDate releaseDate;
    private float popularity;
    private boolean active;
    private LocalDate dateCreated;
    private List<GenresDTO> genres;
    private List<CastDTO> cast;

    public MovieDTO() {
    }

    public MovieDTO(int idTheMovieDb, TypeMovie typeMovie, String language, String title, String description,
                    String posterPath, String backdropPath, LocalDate releaseDate, float popularity, boolean active, LocalDate dateCreated, List<GenresDTO> genres, List<CastDTO> cast) {
        this.idTheMovieDb = idTheMovieDb;
        this.typeMovie = typeMovie;
        this.language = language;
        this.title = title;
        this.description = description;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.active = active;
        this.dateCreated = dateCreated;
        this.genres = genres;
        this.cast = cast;
    }

    public int getIdTheMovieDb() {
        return idTheMovieDb;
    }

    public void setIdTheMovieDb(int idTheMovieDb) {
        this.idTheMovieDb = idTheMovieDb;
    }

    public TypeMovie getTypeMovie() {
        return typeMovie;
    }

    public void setTypeMovie(TypeMovie typeMovie) {
        this.typeMovie = typeMovie;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
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

    public List<GenresDTO> getGenres() {
        return genres;
    }

    public void setGenres(List<GenresDTO> genres) {
        this.genres = genres;
    }

    public List<CastDTO> getCast() {
        return cast;
    }

    public void setCast(List<CastDTO> cast) {
        this.cast = cast;
    }
}
