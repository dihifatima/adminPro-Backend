package com.example.security.ws.converter;

import com.example.security.entity.PorteVisa;
import com.example.security.ws.dto.PorteVisaDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class PorteVisaConverter {
    public PorteVisa map(PorteVisaDto dto){
        PorteVisa entity  =new PorteVisa();
        if (dto != null) {
            BeanUtils.copyProperties(dto,entity);
        }
        return entity;
    }
    public PorteVisaDto map(PorteVisa entity){
        PorteVisaDto dto  =new PorteVisaDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity,dto);
        }
        return dto;
    }
    public List<PorteVisa> mapListDtos(List<PorteVisaDto> dtos){
        return  dtos.stream().map(dto -> map(dto)).collect(Collectors.toList());
    }
    public List<PorteVisaDto> mapListEntities(List<PorteVisa> entities){
        return  entities.stream().map(this::map).collect(Collectors.toList());
    }
}
