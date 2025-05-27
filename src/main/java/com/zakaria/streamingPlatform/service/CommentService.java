package com.zakaria.streamingPlatform.service;

import com.zakaria.streamingPlatform.dto.CommentDTO;
import com.zakaria.streamingPlatform.entities.CommentEntity;
import com.zakaria.streamingPlatform.mapper.CommentMapper;
import com.zakaria.streamingPlatform.repository.CommentRepository;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    public Response<CommentDTO> addComment(CommentDTO commentDTO) {
        try {
            commentDTO.setApproved(true); //For future : maybe add some AI to check
            commentDTO.setDate(LocalDateTime.now());

            CommentEntity commentEntity = commentMapper.convertToEntity(commentDTO);
            commentEntity.setUser(Utils.getCurrentUserEntity());
            commentRepository.save(commentEntity);

            CommentDTO savedComment = commentMapper.convertToModel(commentEntity);
            logger.info("The comment was added successfully id {}", commentDTO.getId());
            return Utils.createResponse(HttpStatus.OK.value(), "Comment add successfully", null, savedComment);
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
                return Utils.createResponse(HttpStatus.OK.value(), "Comment modify successfully", null, savedComment);
            } catch (Exception e) {
                logger.error("Failed to modify comment {}", e.toString());
                return Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to modify comment", null, null);
            }
        }
        logger.warn("The comment you're trying to modify does not exist for the id {}", commentDTO.getId());
        return Utils.createResponse(HttpStatus.NOT_FOUND.value(), "The comment you're trying to modify does not exist", null, null);
    }

    public Response<CommentDTO> deleteComment(Long id) {
        Optional<CommentEntity> optionalCommentEntity = commentRepository.findById(id);

        if(optionalCommentEntity.isPresent()) {
            try {
                commentRepository.deleteById(id);
                logger.info("Comment deleted by id {}", id);
                return Utils.createResponse(HttpStatus.OK.value(), "Comment deleted", null, null);
            } catch (Exception e) {
                logger.error("Error while deleting comment by id {}", id);
                return Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete", null, null);
            }
        }
        logger.warn("The comment you're trying to delete does not exist for the id {}", id);
        return Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "The comment you're trying to delete does not exist", null, null);
    }
}
