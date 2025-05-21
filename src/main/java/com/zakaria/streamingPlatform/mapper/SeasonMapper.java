package com.zakaria.streamingPlatform.mapper;

import com.zakaria.streamingPlatform.dto.SeasonDTO;
import com.zakaria.streamingPlatform.entities.SeasonEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SeasonMapper {
    private final ModelMapper modelMapper;

    public SeasonMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        this.modelMapper.typeMap(SeasonDTO.class, SeasonEntity.class)
                .addMappings(mapper -> {
                    mapper.skip(SeasonEntity::setId);
                });
    }

    public SeasonEntity convertToEntity(SeasonDTO seasonDTO) {
        return modelMapper.map(seasonDTO, SeasonEntity.class);
    }

    public SeasonDTO convertToModel(SeasonEntity seasonEntity) {
        return modelMapper.map(seasonEntity, SeasonDTO.class);
    }
}
