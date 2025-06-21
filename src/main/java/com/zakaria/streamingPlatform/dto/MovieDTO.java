package com.zakaria.streamingPlatform.dto;

import com.zakaria.streamingPlatform.entities.TypeMovie;

import java.time.LocalDate;
import java.util.List;

public class MovieDTO {
    private Long id;
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
    private List<MovieCastDTO> movieCast;
    private int runtime; //movie hours

    public MovieDTO() {
    }

    public MovieDTO(Long id, String title, LocalDate releaseDate, boolean active, String language, String posterPath) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.active = active;
        this.language = language;
        this.posterPath = posterPath;
    }

    public MovieDTO(Long id, int idTheMovieDb, TypeMovie typeMovie, String language, String title, String description,
                    String posterPath, String backdropPath, LocalDate releaseDate, float popularity, boolean active,
                    LocalDate dateCreated, List<GenresDTO> genres, List<MovieCastDTO> movieCast, int runtime) {
        this.id = id;
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
        this.movieCast = movieCast;
        this.runtime = runtime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<MovieCastDTO> getMovieCast() {
        return movieCast;
    }

    public void setMovieCast(List<MovieCastDTO> movieCast) {
        this.movieCast = movieCast;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
}
