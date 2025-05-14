package com.zakaria.streamingPlatform.mapper;

import com.zakaria.streamingPlatform.entities.MovieEntity;
import com.zakaria.streamingPlatform.models.MovieModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {
    private final ModelMapper modelMapper;

    public MovieMapper() {
        this.modelMapper = new ModelMapper();
    }

    public MovieEntity convertToEntity(MovieModel movieModel) {
        return modelMapper.map(movieModel, MovieEntity.class);
    }

    public MovieModel convertToModel(MovieEntity movieEntity) {
        return modelMapper.map(movieEntity, MovieModel.class);
    }
}
