package com.zakaria.streamingPlatform.controller;

import com.zakaria.streamingPlatform.dto.MovieDTO;
import com.zakaria.streamingPlatform.service.MovieService;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.response.ResponsePagination;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return movieService.getAllMovie(pageable);
    }

    @GetMapping("/get-trending-movie")
    public Response<List<MovieDTO>> getTrendingMovie() {
        return movieService.getTrendingMovie();
    }

    @GetMapping("/get-trending-tv-show")
    public Response<List<MovieDTO>> getTrendingTvShow() {
        return movieService.getTrendingTvShow();
    }
}
