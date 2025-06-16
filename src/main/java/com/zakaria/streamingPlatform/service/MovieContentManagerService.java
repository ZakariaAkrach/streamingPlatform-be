package com.zakaria.streamingPlatform.service;

import com.zakaria.streamingPlatform.dto.MovieDTO;
import com.zakaria.streamingPlatform.entities.MovieEntity;
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

import java.util.List;

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
    public ResponsePagination<MovieDTO> getAllMovie(Pageable pageable, TypeMovie typeMovie, String title) {
        Page<MovieEntity> movieEntityPage = null;

        if (title.isEmpty() || title.isBlank()) {
            movieEntityPage = movieRepository.findByTypeMovie(typeMovie, pageable);
        } else {
            movieEntityPage = movieRepository.findByTypeMovieAndTitleContainingIgnoreCase(typeMovie, title, pageable);
        }

        if (!movieEntityPage.isEmpty()) {
            logger.info("Get all movies successfully");
            List<MovieDTO> movieDTO = movieMapper.convertToModel(movieEntityPage.getContent());
            movieDTO.forEach(singleMovie -> {
                singleMovie.setMovieCast(null);
                singleMovie.setGenres(null);
            });
            return Utils.createResponsePagination(HttpStatus.OK.value(), "Get all movies successfully", movieDTO, pageable.getPageNumber(),
                    pageable.getPageSize(), movieEntityPage.getTotalPages(), movieEntityPage.getTotalElements(), movieEntityPage.isLast(), null);
        }
        logger.info("No movie available");
        return Utils.createResponsePagination(HttpStatus.NO_CONTENT.value(), "No movie available", "No movie available");
    }
}
