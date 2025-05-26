package com.example.security.ws.converter;

import com.example.security.entity.DemandeService;
import com.example.security.entity.Creneau;
import com.example.security.ws.dto.DemandeServiceDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DemandeServiceConverter {

    /**
     * Convertit une entité DemandeService en DTO
     */
    public DemandeServiceDto map(DemandeService entity) {
        if (entity == null) {
            return null;
        }

        DemandeServiceDto dto = new DemandeServiceDto();
        dto.setId(entity.getId());
        dto.setRef(entity.getRef());
        dto.setDateSoumission(entity.getDateSoumission());
        dto.setDateRendezvous(entity.getDateRendezvous());
        dto.setStatut(entity.getStatut());

        // Mapping utilisateur
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUserNom(entity.getUser().getFirstname() + " " + entity.getUser().getLastname());
        }

        // Mapping service offert
        if (entity.getServiceOffert() != null) {
            dto.setServiceOffertId(entity.getServiceOffert().getId());
            dto.setServiceOffertNom(entity.getServiceOffert().getName());
        }

        // Mapping créneau - C'est ici que le problème était !
        if (entity.getCreneau() != null) {
            dto.setCreneau(entity.getCreneau().getId());

            // Information lisible sur le créneau (optionnel)
            Creneau creneau = entity.getCreneau();
            String creneauInfo = String.format("%s de %s à %s",
                    creneau.getDateCreneau(),
                    creneau.getHeureDebut(),
                    creneau.getHeureFin());
        }

        return dto;
    }

    /**
     * Convertit un DTO DemandeService en entité
     */
    public DemandeService map(DemandeServiceDto dto) {
        if (dto == null) {
            return null;
        }

        DemandeService entity = new DemandeService();
        entity.setId(dto.getId());
        entity.setRef(dto.getRef());
        entity.setDateSoumission(dto.getDateSoumission());
        entity.setDateRendezvous(dto.getDateRendezvous());
        entity.setStatut(dto.getStatut());

        // Note: Les relations (User, ServiceOffert, Creneau) sont gérées
        // dans le service, pas dans le converter

        return entity;
    }

    /**
     * Convertit une liste d'entités en liste de DTOs
     */
    public List<DemandeServiceDto> mapListEntities(List<DemandeService> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de DTOs en liste d'entités
     */
    public List<DemandeService> mapListDtos(List<DemandeServiceDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}