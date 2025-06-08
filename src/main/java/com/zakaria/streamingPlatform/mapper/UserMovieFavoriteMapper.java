package com.zakaria.streamingPlatform.mapper;

import com.zakaria.streamingPlatform.dto.UserMovieFavoriteDTO;
import com.zakaria.streamingPlatform.entities.UserMovieFavoriteEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMovieFavoriteMapper {
    private final ModelMapper modelMapper;

    public UserMovieFavoriteMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserMovieFavoriteEntity convertToEntity(UserMovieFavoriteDTO userMovieFavoriteDTO) {
        return this.modelMapper.map(userMovieFavoriteDTO, UserMovieFavoriteEntity.class);
    }

    public UserMovieFavoriteDTO convertToModel(UserMovieFavoriteEntity userMovieFavoriteEntity) {
        return this.modelMapper.map(userMovieFavoriteEntity, UserMovieFavoriteDTO.class);
    }
}
