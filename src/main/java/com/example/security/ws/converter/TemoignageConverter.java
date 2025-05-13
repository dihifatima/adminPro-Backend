package com.example.security.ws.converter;

import com.example.security.entity.Temoignage;
import com.example.security.ws.dto.TemoignageDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TemoignageConverter {

    public Temoignage map(TemoignageDto dto) {
        Temoignage entity = new Temoignage();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    public TemoignageDto map(Temoignage entity) {
        TemoignageDto dto = new TemoignageDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }

    public List<Temoignage> mapListDtos(List<TemoignageDto> dtos) {
        return dtos.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public List<TemoignageDto> mapListEntities(List<Temoignage> entities) {
        return entities.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
