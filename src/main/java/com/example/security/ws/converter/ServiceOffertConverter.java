package com.example.security.ws.converter;


import com.example.security.entity.ServiceOffert;
import com.example.security.ws.dto.ServiceOffertDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class ServiceOffertConverter {
    public ServiceOffertDto map(ServiceOffert entity) {
        ServiceOffertDto dto = new ServiceOffertDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }

    public ServiceOffert map(ServiceOffertDto dto) {
        ServiceOffert entity = new ServiceOffert();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    public List<ServiceOffertDto> mapListEntities(List<ServiceOffert> entities) {
        return entities.stream().map(this::map).collect(Collectors.toList());
    }

    public List<ServiceOffert> mapListDtos(List<ServiceOffertDto> dtos) {
        return dtos.stream().map(this::map).collect(Collectors.toList());
    }
}



