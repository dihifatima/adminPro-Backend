package com.example.security.ws.converter;

import com.example.security.entity.BlockActuality;
import com.example.security.ws.dto.BlockActualityDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class BlockActualityConverter {

    public BlockActuality map(BlockActualityDto dto) {
        BlockActuality entity = new BlockActuality();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    public BlockActualityDto map(BlockActuality entity) {
        BlockActualityDto dto = new BlockActualityDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }

    public List<BlockActuality> mapListDtos(List<BlockActualityDto> dtos) {
        return dtos.stream().map(this::map).collect(Collectors.toList());
    }

    public List<BlockActualityDto> mapListEntities(List<BlockActuality> entities) {
        return entities.stream().map(this::map).collect(Collectors.toList());
    }
}