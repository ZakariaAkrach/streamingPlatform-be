package com.zakaria.streamingPlatform.service;

import com.zakaria.streamingPlatform.dto.CommentDTO;
import com.zakaria.streamingPlatform.dto.MovieDTO;
import com.zakaria.streamingPlatform.entities.CommentEntity;
import com.zakaria.streamingPlatform.entities.MovieEntity;
import com.zakaria.streamingPlatform.entities.UserEntity;
import com.zakaria.streamingPlatform.mapper.CommentMapper;
import com.zakaria.streamingPlatform.mapper.MovieMapper;
import com.zakaria.streamingPlatform.repository.CommentRepository;
import com.zakaria.streamingPlatform.repository.UserMovieFavoriteRepository;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardUserService {

    private final UserMovieFavoriteRepository userMovieFavoriteRepository;
    private final CommentRepository commentRepository;
    private final MovieMapper movieMapper;
    private final CommentMapper commentMapper;

    public DashboardUserService(UserMovieFavoriteRepository userMovieFavoriteRepository, CommentRepository commentRepository,
                                MovieMapper movieMapper, CommentMapper commentMapper) {
        this.userMovieFavoriteRepository = userMovieFavoriteRepository;
        this.commentRepository = commentRepository;
        this.movieMapper = movieMapper;
        this.commentMapper = commentMapper;
    }

    private static final Logger logger = LoggerFactory.getLogger(DashboardUserService.class);

    public Response<List<MovieDTO>> getAllFavorite() {
        UserEntity user = Utils.getCurrentUserEntity();
        if (user != null) {
            List<MovieEntity> allFavoriteMovieByUserId = this.userMovieFavoriteRepository.findFavoriteMovieByUserId(Utils.getCurrentUserEntity().getId());

            logger.error("Returned {} favorite movie", allFavoriteMovieByUserId.size());
            return Utils.createResponse(HttpStatus.OK.value(), "Successfully returned favorites movie",
                    null, movieMapper.convertToModel(allFavoriteMovieByUserId));
        }
        logger.error("User not logged");
        return Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "No movie available", null, null);
    }

    public Response<List<CommentDTO>> getAllComment() {
        UserEntity user = Utils.getCurrentUserEntity();
        if (user != null) {
            List<CommentEntity> commentEntityList = commentRepository.getAllCommentByUserId(user.getId());
            if (!commentEntityList.isEmpty()) {
                List<CommentDTO> commentDTO = this.commentMapper.convertToModel(commentEntityList);
                logger.info("Returned {} comments for user ID {}", commentDTO.size(), user.getId());
                return Utils.createResponse(HttpStatus.OK.value(), "Comment returned successfully", null, commentDTO);
            } else {
                logger.info("No comments found for the user ID {}", user.getId());
                return Utils.createResponse(HttpStatus.OK.value(), "Comment not found", null, null);
            }
        }
        logger.error("User not logged");
        return Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "No comment available", null, null);
    }
}
