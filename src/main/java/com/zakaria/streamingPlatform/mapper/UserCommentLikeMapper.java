package com.zakaria.streamingPlatform.mapper;

import com.zakaria.streamingPlatform.dto.UserCommentLikeDTO;
import com.zakaria.streamingPlatform.entities.UserCommentLikeEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserCommentLikeMapper {
    private final ModelMapper modelMapper;

    public UserCommentLikeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserCommentLikeEntity convertToEntity(UserCommentLikeDTO userCommentLikeDTO) {
        return this.modelMapper.map(userCommentLikeDTO, UserCommentLikeEntity.class);
    }

    public UserCommentLikeDTO convertToModel(UserCommentLikeEntity userCommentLikeEntity) {
        return this.modelMapper.map(userCommentLikeEntity, UserCommentLikeDTO.class);
    }
}
