package com.zakaria.streamingPlatform.mapper;

import com.zakaria.streamingPlatform.dto.UserPublicDTO;
import com.zakaria.streamingPlatform.entities.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserPublicMapper {
    private final ModelMapper modelMapper;

    public UserPublicMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserPublicDTO convertToModel(UserEntity userEntity) {
        return this.modelMapper.map(userEntity, UserPublicDTO.class);
    }
}
