package com.zakaria.streamingPlatform.external.detailMovieModels;

import java.util.List;

public class TheMovieDbCreditsModel {
    private List<TheMovieDbCastModel> cast;

    public TheMovieDbCreditsModel() {
    }

    public TheMovieDbCreditsModel(List<TheMovieDbCastModel> cast) {
        this.cast = cast;
    }

    public List<TheMovieDbCastModel> getCast() {
        return cast;
    }

    public void setCast(List<TheMovieDbCastModel> cast) {
        this.cast = cast;
    }
}
