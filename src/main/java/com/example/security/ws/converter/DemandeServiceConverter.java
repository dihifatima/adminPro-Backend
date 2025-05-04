package com.example.security.ws.converter;

import com.example.security.Authentification.user.User;
import com.example.security.dao.ServiceOffertRepository;
import com.example.security.entity.DemandeService;
import com.example.security.entity.EtatDemande;
import com.example.security.entity.Etudiant;
import com.example.security.entity.ServiceOffert;
import com.example.security.ws.dto.DemandeServiceDto;
import com.example.security.ws.dto.EtudiantDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class DemandeServiceConverter {
    public DemandeService map(DemandeServiceDto dto){
        DemandeService entity  =new DemandeService();
        if (dto != null) {
            BeanUtils.copyProperties(dto,entity);
        }
        return entity;
    }
    public DemandeServiceDto map(DemandeService entity){
        DemandeServiceDto dto  =new DemandeServiceDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity,dto);
        }
        return dto;
    }
    public List<DemandeService> mapListDtos(List<DemandeServiceDto> dtos){
        return  dtos.stream().map(dto -> map(dto)).collect(Collectors.toList());
    }
    public List<DemandeServiceDto> mapListEntities(List<DemandeService> entities){
        return  entities.stream().map(this::map).collect(Collectors.toList());
    }
}
