package com.zakaria.streamingPlatform.mapper;

import com.zakaria.streamingPlatform.dto.CommentDTO;
import com.zakaria.streamingPlatform.entities.CommentEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private final ModelMapper modelMapper;

    public CommentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CommentEntity convertToEntity(CommentDTO commentDTO) {
        return modelMapper.map(commentDTO, CommentEntity.class);
    }

    public CommentDTO convertToModel(CommentEntity commentEntity) {
        return modelMapper.map(commentEntity, CommentDTO.class);
    }
}
