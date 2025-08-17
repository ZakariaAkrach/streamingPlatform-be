package com.zakaria.streamingPlatform.controller;

import com.zakaria.streamingPlatform.dto.MovieDTO;
import com.zakaria.streamingPlatform.dto.UserMovieFavoriteDTO;
import com.zakaria.streamingPlatform.dto.UserMovieLikeDTO;
import com.zakaria.streamingPlatform.entities.TypeMovie;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.response.ResponsePagination;
import com.zakaria.streamingPlatform.service.MovieService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/the-movie-db")
    public void addMoviesFromDbApi(@RequestParam(defaultValue = "movie") String typeMovieApi) {
        movieService.addMoviesFromDbApi(typeMovieApi);
    }

    @GetMapping("/get-all-movie")
    public ResponsePagination<MovieDTO> getAllMovie(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "MOVIE") TypeMovie typeMovie,
            @RequestParam(defaultValue = "") String title,
            @RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) List<String> languages) {
        Pageable pageable = PageRequest.of(page, size);
        return movieService.getAllMovie(pageable, typeMovie, title, genres, languages);
    }

    @GetMapping("/get-trending-movie")
    public Response<List<MovieDTO>> getTrendingMovie() {
        return movieService.getTrendingMovie();
    }

    @GetMapping("/get-trending-tv-show")
    public Response<List<MovieDTO>> getTrendingTvShow() {
        return movieService.getTrendingTvShow();
    }

    @GetMapping("isLiked/{id}")
    public Response<UserMovieLikeDTO> isMovieIdLiked(@PathVariable Long id) {
        return this.movieService.isMovieIdLiked(id);
    }

    @PutMapping("like")
    public Response<UserMovieLikeDTO> likeMovie(@RequestBody UserMovieLikeDTO userMovieLikeDTO) {
        return this.movieService.likeMovie(userMovieLikeDTO);
    }

    @GetMapping("isFavorite/{id}")
    public Response<UserMovieFavoriteDTO> isMovieIdFavorite(@PathVariable Long id) {
        return this.movieService.isMovieIdFavorite(id);
    }

    @PutMapping("favorite")
    public Response<UserMovieFavoriteDTO> favoriteMovie(@RequestBody UserMovieFavoriteDTO userMovieFavoriteDTO) {
        return this.movieService.favoriteMovie(userMovieFavoriteDTO);
    }
}
