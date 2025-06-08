package com.zakaria.streamingPlatform.mapper;

import com.zakaria.streamingPlatform.compositeKeys.UserCommentKey;
import com.zakaria.streamingPlatform.dto.CommentDTO;
import com.zakaria.streamingPlatform.entities.CommentEntity;
import com.zakaria.streamingPlatform.entities.UserCommentLikeEntity;
import com.zakaria.streamingPlatform.repository.UserCommentLikeRepository;
import com.zakaria.streamingPlatform.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    private final ModelMapper modelMapper;
    private final UserCommentLikeRepository userCommentLikeRepository;

    public CommentMapper(ModelMapper modelMapper, UserCommentLikeRepository userCommentLikeRepository) {
        this.modelMapper = modelMapper;
        this.userCommentLikeRepository = userCommentLikeRepository;
    }

    public CommentEntity convertToEntity(CommentDTO commentDTO) {
        return modelMapper.map(commentDTO, CommentEntity.class);
    }

    public CommentDTO convertToModel(CommentEntity commentEntity) {
        if (commentEntity.getParentComment() != null) {
            modelMapper.typeMap(CommentEntity.class, CommentDTO.class)
                    .addMappings(mapper -> mapper.skip(CommentDTO::setParentComment));
            CommentDTO commentDTO = modelMapper.map(commentEntity, CommentDTO.class);
            commentDTO.setParentComment(commentEntity.getParentComment().getId());

            return commentDTO;
        }
        return modelMapper.map(commentEntity, CommentDTO.class);
    }

    public List<CommentDTO> convertToModel(List<CommentEntity> commentEntities) {
        return commentEntities.stream()
                .map(this::convertToModelAndSetLiked)
                .collect(Collectors.toList());
    }

    public CommentDTO convertToModelAndSetLiked(CommentEntity commentEntity) {
        UserCommentKey userCommentKey = Utils.buildUserCommentKey(commentEntity.getId());
        if(userCommentKey == null){
            return convertToModel(commentEntity);
        }
        Optional<UserCommentLikeEntity> existingUserCommentLike = userCommentLikeRepository.findById(userCommentKey);
        CommentDTO commentDTO = convertToModel(commentEntity);
        commentDTO.setLikedByCurrentUser(null);
        if (existingUserCommentLike.isPresent()) {
            commentDTO.setLikedByCurrentUser(existingUserCommentLike.get().getLiked());
        }
        return commentDTO;
    }
}
