package com.zakaria.streamingPlatform.models;

import com.zakaria.streamingPlatform.entities.TypeMovie;

import java.time.LocalDate;

public class MovieModel {
    private TypeMovie typeMovie;
    private String language;
    private String title;
    private String description;
    private String posterPath; //img piccolo
    private String backdropPath; //img grande
    private LocalDate releaseDate;
    private boolean active;
    private LocalDate dateCreated;

    public MovieModel() {
    }

    public MovieModel(TypeMovie typeMovie, String language, String title, String description, String posterPath,
                      String backdropPath, LocalDate releaseDate, boolean active, LocalDate dateCreated) {
        this.typeMovie = typeMovie;
        this.language = language;
        this.title = title;
        this.description = description;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.active = active;
        this.dateCreated = dateCreated;
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
