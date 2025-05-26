package com.zakaria.streamingPlatform.dto;

public class MovieCastDTO {
    private Long id;
    private String characterName;
    private CastDTO cast;
    private MovieDTO movieDTO;

    public MovieCastDTO() {
    }

    public MovieCastDTO(Long id, String characterName, CastDTO cast, MovieDTO movieDTO) {
        this.id = id;
        this.characterName = characterName;
        this.cast = cast;
        this.movieDTO = movieDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public CastDTO getCast() {
        return cast;
    }

    public void setCast(CastDTO cast) {
        this.cast = cast;
    }

    public MovieDTO getMovieDTO() {
        return movieDTO;
    }

    public void setMovieDTO(MovieDTO movieDTO) {
        this.movieDTO = movieDTO;
    }
}
