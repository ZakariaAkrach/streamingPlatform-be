package com.zakaria.streamingPlatform.external.detailMovieModels;

public class TheMovieDbCastModel {
    private String name;
    private String original_name;
    private String character;
    private String profile_path;

    public TheMovieDbCastModel() {
    }

    public TheMovieDbCastModel(String name, String original_name, String character, String profile_path) {
        this.name = name;
        this.original_name = original_name;
        this.character = character;
        this.profile_path = profile_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
