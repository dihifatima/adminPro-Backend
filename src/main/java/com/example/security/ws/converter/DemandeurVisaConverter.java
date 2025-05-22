package com.example.security.ws.converter;
import com.example.security.entity.DemandeurVisa;
import com.example.security.ws.dto.DemandeurVisaDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class DemandeurVisaConverter {
    public DemandeurVisa map(DemandeurVisaDto dto){
        DemandeurVisa entity  =new DemandeurVisa();
        if (dto != null) {
            BeanUtils.copyProperties(dto,entity);
        }
        return entity;
    }
    public DemandeurVisaDto map(DemandeurVisa entity){
        DemandeurVisaDto dto  =new DemandeurVisaDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity,dto);
        }
        return dto;
    }
    public List<DemandeurVisa> mapListDtos(List<DemandeurVisaDto> dtos){
        return  dtos.stream().map(dto -> map(dto)).collect(Collectors.toList());
    }
    public List<DemandeurVisaDto> mapListEntities(List<DemandeurVisa> entities){
        return  entities.stream().map(this::map).collect(Collectors.toList());
    }
}
