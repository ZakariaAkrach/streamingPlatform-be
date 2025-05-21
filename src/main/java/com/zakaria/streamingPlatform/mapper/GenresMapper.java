package com.zakaria.streamingPlatform.mapper;

import com.zakaria.streamingPlatform.dto.GenresDTO;
import com.zakaria.streamingPlatform.entities.GenresEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenresMapper {
    private final ModelMapper modelMapper;

    public GenresMapper() {
        this.modelMapper = new ModelMapper();
    }

    public GenresEntity convertToEntity(GenresDTO genresDTO) {
        return modelMapper.map(genresDTO, GenresEntity.class);
    }

    public List<GenresEntity> convertToEntity(List<GenresDTO> genresDTOList) {
        return genresDTOList.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
    }

    public GenresDTO convertToModel(GenresEntity genresEntity) {
        return modelMapper.map(genresEntity, GenresDTO.class);
    }
}
