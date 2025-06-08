package com.zakaria.streamingPlatform.mapper;

import com.zakaria.streamingPlatform.dto.UserMovieLikeDTO;
import com.zakaria.streamingPlatform.entities.UserMovieLikeEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMovieLikeMapper {
    private final ModelMapper modelMapper;

    public UserMovieLikeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserMovieLikeEntity convertToEntity(UserMovieLikeDTO userMovieLikeDTO) {
        return this.modelMapper.map(userMovieLikeDTO, UserMovieLikeEntity.class);
    }

    public UserMovieLikeDTO convertToModel(UserMovieLikeEntity userMovieLikeEntity) {
        return this.modelMapper.map(userMovieLikeEntity, UserMovieLikeDTO.class);
    }
}
