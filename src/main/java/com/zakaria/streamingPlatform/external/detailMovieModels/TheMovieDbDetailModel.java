package com.zakaria.streamingPlatform.external.detailMovieModels;

import java.util.List;

public class TheMovieDbDetailModel {
    private List<TheMovieDbGenresMovieModel> genres;
    private TheMovieDbCreditsModel credits;
    private int runtime; //movie hours
    private List<TheMovieDbSeason> seasons;

    public TheMovieDbDetailModel() {
    }

    public TheMovieDbDetailModel(List<TheMovieDbGenresMovieModel> genres, TheMovieDbCreditsModel credits, int runtime, List<TheMovieDbSeason> seasons) {
        this.genres = genres;
        this.credits = credits;
        this.runtime = runtime;
        this.seasons = seasons;
    }

    public List<TheMovieDbGenresMovieModel> getGenres() {
        return genres;
    }

    public void setGenres(List<TheMovieDbGenresMovieModel> genres) {
        this.genres = genres;
    }

    public TheMovieDbCreditsModel getCredits() {
        return credits;
    }

    public void setCredits(TheMovieDbCreditsModel credits) {
        this.credits = credits;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public List<TheMovieDbSeason> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<TheMovieDbSeason> seasons) {
        this.seasons = seasons;
    }
}
