package com.example.security.ws.converter;

import com.example.security.entity.CreneauDisponibilite;
import com.example.security.ws.dto.CreneauDisponibiliteDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class CreneauDisponibiliteConverter {
    public CreneauDisponibilite map(CreneauDisponibiliteDto dto) {
        CreneauDisponibilite entity = new CreneauDisponibilite();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    public CreneauDisponibiliteDto map(CreneauDisponibilite entity) {
        CreneauDisponibiliteDto dto = new CreneauDisponibiliteDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }

    public List<CreneauDisponibilite> mapListDtos(List<CreneauDisponibiliteDto> dtos) {
        return dtos.stream().map(this::map).collect(Collectors.toList());
    }

    public List<CreneauDisponibiliteDto> mapListEntities(List<CreneauDisponibilite> entities) {
        return entities.stream().map(this::map).collect(Collectors.toList());
    }
}