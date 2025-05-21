package com.zakaria.streamingPlatform.mapper;

import com.zakaria.streamingPlatform.dto.CastDTO;
import com.zakaria.streamingPlatform.entities.CastEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CastMapper {
    private final ModelMapper modelMapper;

    public CastMapper() {
        this.modelMapper = new ModelMapper();
    }

    public CastEntity convertToEntity(CastDTO castDTO) {
        return modelMapper.map(castDTO, CastEntity.class);
    }

    public List<CastEntity> convertToEntity(List<CastDTO> castDTOList) {
        return castDTOList.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
    }

    public CastDTO convertToModel(CastEntity castEntity) {
        return modelMapper.map(castEntity, CastDTO.class);
    }
}
