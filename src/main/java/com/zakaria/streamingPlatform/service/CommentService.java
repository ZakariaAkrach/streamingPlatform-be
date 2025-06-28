package com.zakaria.streamingPlatform.service;

import com.zakaria.streamingPlatform.dto.CommentDTO;
import com.zakaria.streamingPlatform.dto.UserCommentLikeDTO;
import com.zakaria.streamingPlatform.entities.CommentEntity;
import com.zakaria.streamingPlatform.entities.MovieEntity;
import com.zakaria.streamingPlatform.entities.UserCommentLikeEntity;
import com.zakaria.streamingPlatform.entities.UserEntity;
import com.zakaria.streamingPlatform.mapper.CommentMapper;
import com.zakaria.streamingPlatform.mapper.UserCommentLikeMapper;
import com.zakaria.streamingPlatform.repository.CommentRepository;
import com.zakaria.streamingPlatform.repository.MovieRepository;
import com.zakaria.streamingPlatform.repository.UserCommentLikeRepository;
import com.zakaria.streamingPlatform.repository.UserRepository;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MovieRepository movieRepository;
    private final UserCommentLikeRepository userCommentLikeRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final UserCommentLikeMapper userCommentLikeMapper;

    public CommentService(CommentRepository commentRepository, MovieRepository movieRepository,
                          UserCommentLikeRepository userCommentLikeRepository, UserRepository userRepository,
                          CommentMapper commentMapper, UserCommentLikeMapper userCommentLikeMapper) {
        this.commentRepository = commentRepository;
        this.movieRepository = movieRepository;
        this.userCommentLikeRepository = userCommentLikeRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
        this.userCommentLikeMapper = userCommentLikeMapper;
    }

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    public Response<List<CommentDTO>> getAllByContent(Long id) {
        Optional<MovieEntity> existingMovie = movieRepository.findById(id);

        if (existingMovie.isPresent()) {
            List<CommentEntity> allCommentByMovieId = commentRepository.getAllCommentByMovieId(id);
            if (!allCommentByMovieId.isEmpty()) {
                List<CommentDTO> commentDTO = this.commentMapper.convertToModel(allCommentByMovieId);
                logger.info("Returned {} comments for movie ID {}", commentDTO.size(), id);
                return Utils.createResponse(HttpStatus.OK.value(), "Comment returned successfully", null, commentDTO);
            } else {
                logger.info("No comments found for the movie ID {}", id);
                return Utils.createResponse(HttpStatus.OK.value(), "Comment not found", null, null);
            }
        }
        logger.info("The movie ID {} not found", id);
        return Utils.createResponse(HttpStatus.OK.value(), "Movie not found", null, null);
    }

    public Response<CommentDTO> addComment(CommentDTO commentDTO) {
        try {
            Optional<MovieEntity> existingMovie = movieRepository.findById(commentDTO.getMovieId());

            if (existingMovie.isPresent()) {
                commentDTO.setApproved(true); //For future : maybe add some AI to check
                commentDTO.setDate(LocalDateTime.now());

                CommentEntity commentEntity = commentMapper.convertToEntity(commentDTO);
                commentEntity.setUser(Utils.getCurrentUserEntity());
                commentEntity.setMovie(existingMovie.get());
                commentRepository.save(commentEntity);

                CommentDTO savedComment = commentMapper.convertToModel(commentEntity);
                logger.info("The comment was added successfully ID {}", savedComment.getId());
                return Utils.createResponse(HttpStatus.OK.value(), "Comment added successfully", null, savedComment);
            } else {
                logger.info("The movie ID {} not found", commentDTO.getId());
                return Utils.createResponse(HttpStatus.OK.value(), "Movie not found", null, null);
            }
        } catch (Exception e) {
            logger.error("Failed to add comment {}", e.toString());
            return Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to add comment", null, null);
        }
    }

    public Response<CommentDTO> modifyComment(CommentDTO commentDTO) {
        Optional<CommentEntity> optionalCommentEntity = commentRepository.findById(commentDTO.getId());

        if (optionalCommentEntity.isPresent()) {
            optionalCommentEntity.get().setContent(commentDTO.getContent());
            try {
                CommentEntity commentEntity = commentRepository.save(optionalCommentEntity.get());
                CommentDTO savedComment = commentMapper.convertToModel(commentEntity);
                logger.info("The comment was updated successfully for id {}", commentDTO.getId());
                return Utils.createResponse(HttpStatus.OK.value(), "Comment modified successfully", null, savedComment);
            } catch (Exception e) {
                logger.error("Failed to modify comment {}", e.toString());
                return Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to modify comment", null, null);
            }
        }
        logger.warn("The comment you're trying to modify does not exist for the id {}", commentDTO.getId());
        return Utils.createResponse(HttpStatus.NOT_FOUND.value(), "Comment not found for modification", null, null);
    }

    public Response<UserCommentLikeDTO> likeComment(UserCommentLikeDTO userCommentLikeDTO) {
        Optional<CommentEntity> existingComment = commentRepository.findById(userCommentLikeDTO.getCommentId());
        Optional<UserEntity> existingUser = userRepository.findById(Utils.getCurrentUserEntity().getId());

        if (existingComment.isPresent() && existingUser.isPresent()) {
            try {
                Optional<UserCommentLikeEntity> existingUserCommentLike = userCommentLikeRepository.findById(Utils.buildUserCommentKey(existingComment.get().getId()));
                UserCommentLikeEntity userCommentLikeEntity = null;
                UserCommentLikeEntity savedUserCommentLikeEntity = null;
                if (existingUserCommentLike.isPresent()) {
                    userCommentLikeEntity = existingUserCommentLike.get();
                    if (userCommentLikeEntity.getLiked() != null && userCommentLikeEntity.getLiked().equals(userCommentLikeDTO.isLiked())) {
                        userCommentLikeEntity.setLiked(null);
                    } else {
                        userCommentLikeEntity.setLiked(userCommentLikeDTO.isLiked());
                    }
                    savedUserCommentLikeEntity = userCommentLikeRepository.save(userCommentLikeEntity);
                } else {
                    userCommentLikeEntity = new UserCommentLikeEntity();
                    userCommentLikeEntity.setUserCommentKey(Utils.buildUserCommentKey(existingComment.get().getId()));
                    userCommentLikeEntity.setLiked(userCommentLikeDTO.isLiked());
                    userCommentLikeEntity.setComment(existingComment.get());
                    userCommentLikeEntity.setUser(existingUser.get());
                    savedUserCommentLikeEntity = userCommentLikeRepository.save(userCommentLikeEntity);
                }
                UserCommentLikeDTO savedUserCommentLikeDTO = userCommentLikeMapper.convertToModel(savedUserCommentLikeEntity);
                logger.info("Add like to the comment ID {} and user ID {}", userCommentLikeDTO.getCommentId(), Utils.getCurrentUserEntity().getId());
                return Utils.createResponse(HttpStatus.OK.value(), "Successfully added like", null, savedUserCommentLikeDTO);
            } catch (Exception e) {
                logger.error("Error while adding like to the comment ID {}", userCommentLikeDTO.getCommentId());
                return Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to add like", null, null);
            }
        }
        logger.error("Comment ID {} not found", userCommentLikeDTO.getCommentId());
        return Utils.createResponse(HttpStatus.NOT_FOUND.value(), "Comment to add like not found", null, null);
    }

    public Response<CommentDTO> replyComment(CommentDTO commentDTO) {
        Optional<MovieEntity> existingMovie = movieRepository.findById(commentDTO.getMovieId());

        if (existingMovie.isPresent()) {
            Optional<CommentEntity> existingComment = commentRepository.findById(commentDTO.getId());
            if (existingComment.isPresent()) {
                try {
                    CommentEntity commentEntity = new CommentEntity();
                    commentEntity.setContent(commentDTO.getContent());
                    commentEntity.setApproved(true); //For future : maybe add some AI to check
                    commentEntity.setDate(LocalDateTime.now());
                    commentEntity.setUser(Utils.getCurrentUserEntity());
                    commentEntity.setMovie(existingMovie.get());
                    commentEntity.setParentComment(existingComment.get());

                    CommentDTO savedComment = commentMapper.convertToModel(commentRepository.save(commentEntity));
                    logger.info("Comment reply successfully to id {}", commentDTO.getId());
                    return Utils.createResponse(HttpStatus.OK.value(), "Comment reply successfully", null, savedComment);
                } catch (Exception e) {
                    logger.info("Failed to reply at ID {} error {} ", commentDTO.getId(), e.getMessage());
                    return Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to reply", null, null);
                }
            } else {
                logger.info("Comment to reply not found by id {}", commentDTO.getId());
                return Utils.createResponse(HttpStatus.NOT_FOUND.value(), "Comment to reply not found", null, null);
            }
        }
        logger.info("Movie not found by id {}", commentDTO.getMovieId());
        return Utils.createResponse(HttpStatus.NOT_FOUND.value(), "Failed not found movie", null, null);
    }

    @Transactional
    public Response<String> deleteCommentById(Long id) {
        Optional<CommentEntity> existingComment = commentRepository.findById(id);
        if (existingComment.isPresent()) {

            List<CommentEntity> childComments = commentRepository.findByParentComment(existingComment.get());
            try {
                for (CommentEntity childComment : childComments) {
                    userCommentLikeRepository.deleteAllByCommentId(childComment.getId());
                    deleteById(childComment.getId());
                }
                userCommentLikeRepository.deleteAllByCommentId(id);
                deleteById(id);
                return Utils.createResponse(HttpStatus.OK.value(), "Deleted comment successfully");

            } catch (Exception e) {
                logger.error("Error while deleting comment ID {} successfully, cause: {}", id, e.getMessage());
                return Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete comment");
            }
        }
        logger.info("Comment ID {} not found", id);
        return Utils.createResponse(HttpStatus.NOT_FOUND.value(), "Comment not found");
    }

    private void deleteById(Long id) {
        commentRepository.deleteById(id);
        logger.info("Deleted comment ID {}", id);
    }
}
