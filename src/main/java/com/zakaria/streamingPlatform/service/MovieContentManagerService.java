package com.zakaria.streamingPlatform.service;

import com.zakaria.streamingPlatform.dto.MovieDTO;
import com.zakaria.streamingPlatform.entities.TypeMovie;
import com.zakaria.streamingPlatform.mapper.MovieMapper;
import com.zakaria.streamingPlatform.repository.MovieRepository;
import com.zakaria.streamingPlatform.response.ResponsePagination;
import com.zakaria.streamingPlatform.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MovieContentManagerService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieContentManagerService(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    private static final Logger logger = LoggerFactory.getLogger(MovieContentManagerService.class);

    @Cacheable("allMovieContentManager")
    public ResponsePagination<MovieDTO> getAllMovie(Pageable pageable, TypeMovie typeMovie) {

        Page<MovieDTO> movieEntityPage = movieRepository.findMoviesByTypeMovie(typeMovie, pageable);

        if (!movieEntityPage.isEmpty()) {
            logger.info("Get all movies successfully");
            return Utils.createResponsePagination(HttpStatus.OK.value(), "Get all movies successfully", movieEntityPage.getContent(), pageable.getPageNumber(),
                    pageable.getPageSize(), movieEntityPage.getTotalPages(), movieEntityPage.getTotalElements(), movieEntityPage.isLast(), null);
        }
        logger.info("No movie available");
        return Utils.createResponsePagination(HttpStatus.NO_CONTENT.value(), "No movie available", "No movie available");
    }
}
