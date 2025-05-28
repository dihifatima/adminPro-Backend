package com.example.security.ws.converter;
import com.example.security.entity.Creneau;
import com.example.security.ws.dto.CreneauDto;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreneauConverter {

    public CreneauDto map(Creneau entity) {
        if (entity == null) {
            return null;
        }

        CreneauDto dto = new CreneauDto();
        dto.setId(entity.getId());
        dto.setDateCreneau(entity.getDateCreneau());
        dto.setHeureDebut(entity.getHeureDebut());
        dto.setHeureFin(entity.getHeureFin());
        dto.setCapaciteMax(entity.getCapaciteMax());
        dto.setActif(entity.getActif());

        if (entity.getCreneauDisponibilite() != null) {
            dto.setCreneauDisponibiliteId(entity.getCreneauDisponibilite().getId());
        }

        return dto;
    }

    public Creneau map(CreneauDto dto) {
        if (dto == null) {
            return null;
        }

        Creneau entity = new Creneau();
        entity.setId(dto.getId());
        entity.setDateCreneau(dto.getDateCreneau());
        entity.setHeureDebut(dto.getHeureDebut());
        entity.setHeureFin(dto.getHeureFin());
        entity.setCapaciteMax(dto.getCapaciteMax());
        entity.setActif(dto.getActif());


        return entity;
    }

    public List<CreneauDto> mapListEntities(List<Creneau> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public List<Creneau> mapListDtos(List<CreneauDto> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}