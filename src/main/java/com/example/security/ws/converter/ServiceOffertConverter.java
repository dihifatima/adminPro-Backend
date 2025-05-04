package com.example.security.ws.converter;

import com.example.security.entity.ServiceOffert;
import com.example.security.entity.TypeService;
import com.example.security.ws.dto.ServiceOffertDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceOffertConverter {

    public ServiceOffert map(ServiceOffertDto dto){
        ServiceOffert entity = new ServiceOffert();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity, "type"); // on ignore "type"
            if (dto.getType() != null) {
                entity.setType(TypeService.valueOf(dto.getType())); // conversion manuelle
            }
        }
        return entity;
    }

    public ServiceOffertDto map(ServiceOffert entity){
        ServiceOffertDto dto = new ServiceOffertDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto, "type"); // on ignore "type"
            if (entity.getType() != null) {
                dto.setType(entity.getType().name()); // conversion enum -> String
            }
        }
        return dto;
    }

    public List<ServiceOffert> mapListDtos(List<ServiceOffertDto> dtos){
        return (dtos == null) ? List.of() : dtos.stream().map(this::map).collect(Collectors.toList());
    }

    public List<ServiceOffertDto> mapListEntities(List<ServiceOffert> entities){
        return (entities == null) ? List.of() : entities.stream().map(this::map).collect(Collectors.toList());
    }
}
