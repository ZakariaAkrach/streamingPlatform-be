package com.zakaria.streamingPlatform.external.detailMovieModels;

import java.time.LocalDate;

public class TheMovieDbSeason {
    private LocalDate air_date;
    private int episode_count;
    private int id;
    private int season_number;

    public TheMovieDbSeason() {
    }

    public TheMovieDbSeason(LocalDate air_date, int episode_count, int id, int season_number) {
        this.air_date = air_date;
        this.episode_count = episode_count;
        this.id = id;
        this.season_number = season_number;
    }

    public LocalDate getAir_date() {
        return air_date;
    }

    public void setAir_date(LocalDate air_date) {
        this.air_date = air_date;
    }

    public int getEpisode_count() {
        return episode_count;
    }

    public void setEpisode_count(int episode_count) {
        this.episode_count = episode_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeason_number() {
        return season_number;
    }

    public void setSeason_number(int season_number) {
        this.season_number = season_number;
    }
}
