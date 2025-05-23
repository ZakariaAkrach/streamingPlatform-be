package com.zakaria.streamingPlatform.mapper;

import com.zakaria.streamingPlatform.entities.UserEntity;
import com.zakaria.streamingPlatform.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserMapper() {
        this.modelMapper = new ModelMapper();
    }

    public UserEntity convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, UserEntity.class);
    }

    public UserDTO convertToModel(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDTO.class);
    }
}
