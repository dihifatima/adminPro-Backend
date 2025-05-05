package com.example.security.ws.converter;


import com.example.security.entity.ServiceOffert;
import com.example.security.ws.dto.ServiceOffertDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class ServiceOffertConverter {
    // ترسل كل البيانات إلى الإداري:
   //map(entity)	ينسخ كل شيء من الكيان إلى الـ DTO (للعرض فقط).
    public ServiceOffertDto map(ServiceOffert entity) {
        ServiceOffertDto dto = new ServiceOffertDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }

    public ServiceOffert map(ServiceOffertDto dto) {
        if (dto == null) return null;
        ServiceOffert entity = new ServiceOffert();
        entity.setName(dto.getName());
        // On ne copie pas id, createdAt, lastModifiedAt car ils sont gérés par JPA
        return entity;
    }

    public List<ServiceOffertDto> mapListEntities(List<ServiceOffert> entities) {
        return entities.stream().map(this::map).collect(Collectors.toList());
    }

    public List<ServiceOffert> mapListDtos(List<ServiceOffertDto> dtos) {
        return dtos.stream().map(this::map).collect(Collectors.toList());
    }
}



