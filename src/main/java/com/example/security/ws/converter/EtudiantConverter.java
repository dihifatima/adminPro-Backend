package com.example.security.ws.converter;

import com.example.security.entity.Etudiant;
import com.example.security.ws.dto.EtudiantDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class EtudiantConverter {
    public Etudiant map(EtudiantDto dto){
        Etudiant entity  =new Etudiant();
        if (dto != null) {
            BeanUtils.copyProperties(dto,entity);
        }
        return entity;
    }
    public EtudiantDto map(Etudiant entity){
        EtudiantDto dto  =new EtudiantDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity,dto);
        }
        return dto;
    }
    public List<Etudiant> mapListDtos(List<EtudiantDto> dtos){
        return  dtos.stream().map(dto -> map(dto)).collect(Collectors.toList());
    }
    public List<EtudiantDto> mapListEntities(List<Etudiant> entities){
        return  entities.stream().map(this::map).collect(Collectors.toList());
    }
}
