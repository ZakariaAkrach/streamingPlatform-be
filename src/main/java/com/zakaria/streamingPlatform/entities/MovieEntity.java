package com.zakaria.streamingPlatform.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "movie")
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int idTheMovieDb;
    private TypeMovie typeMovie;
    private String language;
    private String title;
    @Lob
    private String description;
    private String posterPath; //img small
    private String backdropPath; //img big
    private LocalDate releaseDate;
    private float popularity;
    private int runtime; //movie hours
    private Integer likes = 0;
    private Integer dislike = 0;

    @OneToMany(mappedBy = "movie")
    private List<SeasonEntity> seasons;

    private boolean active;

    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<GenresEntity> genres;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<MovieCastEntity> movieCast;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<CommentEntity> comments;

    private LocalDate dateCreated;


    @PrePersist
    public void prePersist() {
        if (likes == null) likes = 0;
        if (dislike == null) dislike = 0;
    }


    public MovieEntity() {
    }

    public MovieEntity(Long id, int idTheMovieDb, TypeMovie typeMovie, String language, String title, String description,
                       String posterPath, String backdropPath, LocalDate releaseDate, float popularity, int runtime,
                       Integer likes, Integer dislike, List<SeasonEntity> seasons, boolean active, List<GenresEntity> genres,
                       List<MovieCastEntity> movieCast, List<CommentEntity> comments, LocalDate dateCreated) {
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
        this.runtime = runtime;
        this.likes = likes;
        this.dislike = dislike;
        this.seasons = seasons;
        this.active = active;
        this.genres = genres;
        this.movieCast = movieCast;
        this.comments = comments;
        this.dateCreated = dateCreated;
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

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislike() {
        return dislike;
    }

    public void setDislike(Integer dislike) {
        this.dislike = dislike;
    }

    public List<SeasonEntity> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<SeasonEntity> seasons) {
        this.seasons = seasons;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<GenresEntity> getGenres() {
        return genres;
    }

    public void setGenres(List<GenresEntity> genres) {
        this.genres = genres;
    }

    public List<MovieCastEntity> getMovieCast() {
        return movieCast;
    }

    public void setMovieCast(List<MovieCastEntity> movieCast) {
        this.movieCast = movieCast;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
}
