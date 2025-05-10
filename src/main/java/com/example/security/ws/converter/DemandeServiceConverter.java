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
            // Copie des propriétés simples
            BeanUtils.copyProperties(entity, dto);

            // Champs personnalisés à partir des associations
            if (entity.getUser() != null) {
                dto.setUserId(entity.getUser().getId());
                dto.setUserNom(entity.getUser().getFullName()); // ✅ nom complet
            }

            if (entity.getServiceOffert() != null) {
                dto.setServiceOffertId(entity.getServiceOffert().getId());
                dto.setServiceOffertNom(entity.getServiceOffert().getName()); // ✅ nom du service
            }
        }
        return dto;
    }

    public DemandeService map(DemandeServiceDto dto) {
        DemandeService entity = new DemandeService();
        if (dto != null) {
            // On copie uniquement les champs nécessaires à la création
            entity.setDateRendezvous(dto.getDateRendezvous());
            // ref, user, serviceOffert, statut, dateSoumission seront complétés dans le service
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
