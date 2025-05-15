package com.example.security.ws.converter;

import com.example.security.entity.BlocActualites;
import com.example.security.ws.dto.BlocActualitesDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlocActualitesConverter {

    public BlocActualites map(BlocActualitesDto dto) {
        BlocActualites entity = new BlocActualites();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    public BlocActualitesDto map(BlocActualites entity) {
        BlocActualitesDto dto = new BlocActualitesDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }

    public List<BlocActualites> mapListDtos(List<BlocActualitesDto> dtos) {
        return dtos.stream().map(this::map).collect(Collectors.toList());
    }

    public List<BlocActualitesDto> mapListEntities(List<BlocActualites> entities) {
        return entities.stream().map(this::map).collect(Collectors.toList());
    }
}
