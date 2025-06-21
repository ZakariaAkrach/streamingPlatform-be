package com.zakaria.streamingPlatform.mapper;

import com.zakaria.streamingPlatform.dto.MovieDTO;
import com.zakaria.streamingPlatform.entities.MovieEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieMapper {
    private final ModelMapper modelMapper;

    public MovieMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        this.modelMapper.typeMap(MovieDTO.class, MovieEntity.class)
                .addMappings(mapper -> {
                    mapper.skip(MovieEntity::setId);
                });
    }

    public MovieEntity convertToEntity(MovieDTO movieDTO) {
        return modelMapper.map(movieDTO, MovieEntity.class);
    }

    public MovieDTO convertToModel(MovieEntity movieEntity) {
        return modelMapper.map(movieEntity, MovieDTO.class);
    }

    public List<MovieDTO> convertToModel(List<MovieEntity> movieEntity) {
        return movieEntity.stream().map(this::convertToModel).collect(Collectors.toList());
    }
}
