package com.zakaria.streamingPlatform.controller;

import com.zakaria.streamingPlatform.dto.MovieDTO;
import com.zakaria.streamingPlatform.entities.TypeMovie;
import com.zakaria.streamingPlatform.response.ResponsePagination;
import com.zakaria.streamingPlatform.service.MovieContentManagerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(defaultValue = "MOVIE") TypeMovie typeMovie) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return this.movieContentManagerService.getAllMovie(pageable, typeMovie);
    }
}
