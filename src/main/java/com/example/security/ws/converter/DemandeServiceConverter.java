package com.example.security.ws.converter;

import com.example.security.entity.DemandeService;
import com.example.security.ws.dto.DemandeServiceDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class DemandeServiceConverter {
    public DemandeServiceDto map(DemandeService entity) {
        DemandeServiceDto dto = new DemandeServiceDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
            if (entity.getUser() != null)
                dto.setUserId(entity.getUser().getId());
            if (entity.getServiceOffert() != null)
                dto.setServiceOffertId(entity.getServiceOffert().getId());
        }
        return dto;
    }

    public DemandeService map(DemandeServiceDto dto) {
        DemandeService entity = new DemandeService();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
            // Associations (user, serviceOffert) will be set in the service layer
        }
        return entity;
    }

    public List<DemandeServiceDto> mapListEntities(List<DemandeService> entities) {
        return entities.stream().map(this::map).collect(Collectors.toList());
    }

    public List<DemandeService> mapListDtos(List<DemandeServiceDto> dtos) {
        return dtos.stream().map(this::map).collect(Collectors.toList());
    }
}