package com.zakaria.streamingPlatform.dto;

import java.time.LocalDate;

public class SeasonDTO {
    private LocalDate airDate;
    private int episodeCount;
    private int idTheMovieDb;
    private int seasonNumber;

    public SeasonDTO() {
    }

    public SeasonDTO(LocalDate airDate, int episodeCount, int idTheMovieDb, int seasonNumber) {
        this.airDate = airDate;
        this.episodeCount = episodeCount;
        this.idTheMovieDb = idTheMovieDb;
        this.seasonNumber = seasonNumber;
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
}
