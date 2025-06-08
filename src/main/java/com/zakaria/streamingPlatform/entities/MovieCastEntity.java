package com.zakaria.streamingPlatform.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "movie_cast")
public class MovieCastEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;

    @ManyToOne
    @JoinColumn(name = "cast_id")
    private CastEntity cast;

    private String characterName;

    public MovieCastEntity() {
    }

    public MovieCastEntity(Long id, MovieEntity movie, CastEntity cast, String characterName) {
        this.id = id;
        this.movie = movie;
        this.cast = cast;
        this.characterName = characterName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovieEntity getMovie() {
        return movie;
    }

    public void setMovie(MovieEntity movie) {
        this.movie = movie;
    }

    public CastEntity getCast() {
        return cast;
    }

    public void setCast(CastEntity cast) {
        this.cast = cast;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }
}
