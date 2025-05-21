package com.example.security.ws.converter;
import com.example.security.entity.Particulier;
import com.example.security.ws.dto.ParticulierDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class ParticulierConverter {
    public Particulier map(ParticulierDto dto){
        Particulier entity  =new Particulier();
        if (dto != null) {
            BeanUtils.copyProperties(dto,entity);
        }
        return entity;
    }
    public ParticulierDto map(Particulier entity){
        ParticulierDto dto  =new ParticulierDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity,dto);
        }
        return dto;
    }
    public List<Particulier> mapListDtos(List<ParticulierDto> dtos){
        return  dtos.stream().map(dto -> map(dto)).collect(Collectors.toList());
    }
    public List<ParticulierDto> mapListEntities(List<Particulier> entities){
        return  entities.stream().map(this::map).collect(Collectors.toList());
    }
}
