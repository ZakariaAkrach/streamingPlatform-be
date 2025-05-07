package com.zakaria.streamingPlatform.mapper;

import com.zakaria.streamingPlatform.entities.UserEntity;
import com.zakaria.streamingPlatform.models.UserModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserMapper() {
        this.modelMapper = new ModelMapper();
    }

    public UserEntity convertToEntity(UserModel userModel) {
        return modelMapper.map(userModel, UserEntity.class);
    }

    public UserModel convertToModel(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserModel.class);
    }
}
