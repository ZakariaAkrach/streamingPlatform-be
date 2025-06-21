package com.zakaria.streamingPlatform.controller;

import com.zakaria.streamingPlatform.dto.MovieDTO;
import com.zakaria.streamingPlatform.entities.TypeMovie;
import com.zakaria.streamingPlatform.external.TheMovieDbModel;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.response.ResponsePagination;
import com.zakaria.streamingPlatform.service.MovieContentManagerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("content-manager")
public class MovieContentManagerController {

    private final MovieContentManagerService movieContentManagerService;

    public MovieContentManagerController(MovieContentManagerService movieContentManagerService) {
        this.movieContentManagerService = movieContentManagerService;
    }

    @GetMapping("/get-all-movie")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    public ResponsePagination<MovieDTO> getAllMovie(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(defaultValue = "MOVIE") TypeMovie typeMovie,
            @RequestParam(defaultValue = "") String title) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return this.movieContentManagerService.getAllMovie(pageable, typeMovie, title);
    }

    @GetMapping("/get-movie-from-the-movie-db")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    public List<TheMovieDbModel> getMovieFromTheMovieDb(
            @RequestParam(defaultValue = "movie") String typeMovie,
            @RequestParam(defaultValue = "en") String language
    ) {
        return this.movieContentManagerService.getRandomMoviesFromTheMovieDb(typeMovie, language);
    }

    @PostMapping("/add-movie-from-the-movie-db-id")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    public Response<String> addSingleMovieFromTheMovieDb(
            @RequestParam() int id,
            @RequestParam() String typeMovie
    ) {
        return this.movieContentManagerService.addSingleMovieFromTheMovieDb(typeMovie, id);
    }

    @PutMapping("/modify")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    public Response<String> modifyContent(@RequestBody MovieDTO movieDTO) {
        return this.movieContentManagerService.modifyContent(movieDTO);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    public Response<String> deleteContent(@PathVariable(name = "id") Long id) {
        return this.movieContentManagerService.deleteContent(id);
    }
}
