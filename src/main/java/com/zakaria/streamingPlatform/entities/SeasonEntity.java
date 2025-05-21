package com.zakaria.streamingPlatform.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "season")
public class SeasonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate airDate;
    private int episodeCount;
    private int idTheMovieDb;
    private int seasonNumber;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;

    public SeasonEntity() {
    }

    public SeasonEntity(Long id, LocalDate airDate, int episodeCount, int idTheMovieDb, int seasonNumber, MovieEntity movie) {
        this.id = id;
        this.airDate = airDate;
        this.episodeCount = episodeCount;
        this.idTheMovieDb = idTheMovieDb;
        this.seasonNumber = seasonNumber;
        this.movie = movie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAirDate() {
        return airDate;
    }

    public void setAirDate(LocalDate airDate) {
        this.airDate = airDate;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }

    public int getIdTheMovieDb() {
        return idTheMovieDb;
    }

    public void setIdTheMovieDb(int idTheMovieDb) {
        this.idTheMovieDb = idTheMovieDb;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public MovieEntity getMovie() {
        return movie;
    }

    public void setMovie(MovieEntity movie) {
        this.movie = movie;
    }
}
