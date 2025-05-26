package com.example.security.ws.converter;

import com.example.security.entity.Creneau;
import com.example.security.ws.dto.CreneauDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreneauConverter {

    /**
     * Convertit un objet Creneau (entité) vers CreneauDto
     */
    public CreneauDto map(Creneau entity) {
        if (entity == null) {
            return null;
        }

        CreneauDto dto = new CreneauDto();
        dto.setId(entity.getId());
        dto.setCreneauDisponibilite(entity.getCreneauDisponibilite());
        dto.setDateCreneau(entity.getDateCreneau());
        dto.setHeureDebut(entity.getHeureDebut());
        dto.setHeureFin(entity.getHeureFin());
        dto.setCapaciteRestante(entity.getCapaciteRestante());
        dto.setActif(entity.getActif());

        return dto;
    }

    /**
     * Convertit un objet CreneauDto vers Creneau (entité)
     */
    public Creneau map(CreneauDto dto) {
        if (dto == null) {
            return null;
        }

        Creneau entity = new Creneau();
        entity.setId(dto.getId());
        entity.setCreneauDisponibilite(dto.getCreneauDisponibilite());
        entity.setDateCreneau(dto.getDateCreneau());
        entity.setHeureDebut(dto.getHeureDebut());
        entity.setHeureFin(dto.getHeureFin());
        entity.setCapaciteRestante(dto.getCapaciteRestante());
        entity.setActif(dto.getActif());

        return entity;
    }

    /**
     * Convertit une liste d'entités Creneau vers une liste de CreneauDto
     */
    public List<CreneauDto> mapListEntities(List<Creneau> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de CreneauDto vers une liste d'entités Creneau
     */
    public List<Creneau> mapListDtos(List<CreneauDto> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
