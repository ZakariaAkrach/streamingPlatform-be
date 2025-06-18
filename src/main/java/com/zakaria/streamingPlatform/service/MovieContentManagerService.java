package com.zakaria.streamingPlatform.service;

import com.zakaria.streamingPlatform.dto.MovieDTO;
import com.zakaria.streamingPlatform.entities.MovieEntity;
import com.zakaria.streamingPlatform.entities.TypeMovie;
import com.zakaria.streamingPlatform.mapper.MovieMapper;
import com.zakaria.streamingPlatform.repository.*;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.response.ResponsePagination;
import com.zakaria.streamingPlatform.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MovieContentManagerService {

    private final MovieRepository movieRepository;
    private final UserMovieLikeRepository userMovieLikeRepository;
    private final UserMovieFavoriteRepository userMovieFavoriteRepository;
    private final CommentRepository commentRepository;
    private final UserCommentLikeRepository userCommentLikeRepository;
    private final MovieMapper movieMapper;


    public MovieContentManagerService(MovieRepository movieRepository, UserMovieLikeRepository userMovieLikeRepository,
                                      UserMovieFavoriteRepository userMovieFavoriteRepository, CommentRepository commentRepository,
                                      UserCommentLikeRepository userCommentLikeRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.userMovieLikeRepository = userMovieLikeRepository;
        this.userMovieFavoriteRepository = userMovieFavoriteRepository;
        this.commentRepository = commentRepository;
        this.userCommentLikeRepository = userCommentLikeRepository;
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


    @CacheEvict(value = "allMovieContentManager", allEntries = true)
    public Response<String> modifyContent(MovieDTO movieDTO) {
        Optional<MovieEntity> existMovie = this.movieRepository.findById(movieDTO.getId());

        if (existMovie.isPresent()) {
            try {
                MovieEntity movieEntity = existMovie.get();
                movieEntity.setTitle(movieDTO.getTitle());
                movieEntity.setDescription(movieDTO.getDescription());
                movieEntity.setActive(movieDTO.isActive());
                movieEntity.setLanguage(movieDTO.getLanguage());
                movieEntity.setReleaseDate(movieDTO.getReleaseDate());
                movieEntity.setRuntime(movieDTO.getRuntime());
                movieEntity.setTypeMovie(movieDTO.getTypeMovie());

                this.movieRepository.save(movieEntity);

                logger.info("Successfully updated movie with ID: {} ", movieDTO.getId());
                return Utils.createResponse(HttpStatus.OK.value(), "Movie updated successfully.");
            } catch (Exception e) {
                logger.info("Error while updating movie id {} ", movieDTO.getId());
                return Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while updating movie");
            }
        } else {
            logger.info("Movie id {} not found ", movieDTO.getId());
            return Utils.createResponse(HttpStatus.NO_CONTENT.value(), "Movie not found");
        }
    }

    @CacheEvict(value = {"allMovieContentManager", "allMovie"}, allEntries = true)
    @Transactional
    public Response<String> deleteContent(Long id) {
        Optional<MovieEntity> existMovie = this.movieRepository.findById(id);

        if (existMovie.isPresent()) {
            try {
                //It is made on purpose the content manager role can delete movie even if they have like comment...
                this.userMovieLikeRepository.deleteAllByMovieId(id);
                this.userMovieFavoriteRepository.deleteAllByMovieId(id);
                this.userCommentLikeRepository.deleteAllByMovieId(id);
                this.commentRepository.deleteAllByMovieId(id);

                this.movieRepository.deleteById(id);
                logger.info("Successfully deleted movie with ID: {} ", id);
                return Utils.createResponse(HttpStatus.OK.value(), "Movie deleted successfully.");
            } catch (Exception e) {
                logger.error("Error while deleted movie with ID: {} error {} ", id, e.getMessage());
                return Utils.createResponse(HttpStatus.OK.value(), "Error deleting movie: " + e.getMessage());
            }
        } else {
            logger.info("Movie id {} not found ", id);
            return Utils.createResponse(HttpStatus.NO_CONTENT.value(), "Movie not found");
        }
    }
}
