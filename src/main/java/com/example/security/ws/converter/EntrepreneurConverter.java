package com.example.security.ws.converter;

import com.example.security.entity.Entrepreneur;
import com.example.security.ws.dto.EntrepreneurDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class EntrepreneurConverter {
    public Entrepreneur map(EntrepreneurDto dto) {
        Entrepreneur entity = new Entrepreneur();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    public EntrepreneurDto map(Entrepreneur entity) {
        EntrepreneurDto dto = new EntrepreneurDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }

    public List<Entrepreneur> mapListDtos(List<EntrepreneurDto> dtos) {
        return dtos.stream().map(dto -> map(dto)).collect(Collectors.toList());
    }

    public List<EntrepreneurDto> mapListEntities(List<Entrepreneur> entities) {
        return entities.stream().map(this::map).collect(Collectors.toList());
    }
}