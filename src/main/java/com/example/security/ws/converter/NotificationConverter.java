package com.example.security.ws.converter;

import com.example.security.Authentification.user.User;
import com.example.security.entity.Notification;
import com.example.security.ws.dto.NotificationDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationConverter {

    public NotificationDto toDto(Notification entity) {
        if (entity == null) {
            return null;
        }

        NotificationDto dto = new NotificationDto();
        dto.setIdNotification(entity.getIdNotification());
        dto.setSujet(entity.getSujet());
        dto.setMessage(entity.getMessage());
        dto.setDateCreation(entity.getDateCreation());
        dto.setVue(entity.isVue());

        // Conversion des relations
        User destinataire = entity.getDestinataire();
        if (destinataire != null) {
            dto.setDestinataireId(destinataire.getId());
            dto.setDestinataireNom(destinataire.getFirstname() + " " + destinataire.getLastname());
        }

        if (entity.getDemandeService() != null) {
            dto.setDemandeServiceId(entity.getDemandeService().getIdDemande());
        }

        return dto;
    }

    public Notification toEntity(NotificationDto dto) {
        if (dto == null) {
            return null;
        }

        Notification entity = new Notification();
        entity.setIdNotification(dto.getIdNotification());
        entity.setSujet(dto.getSujet());
        entity.setMessage(dto.getMessage());
        entity.setDateCreation(dto.getDateCreation());
        entity.setVue(dto.isVue());

        // Note: Les relations (destinataire et demandeService) doivent être définies
        // par le service après récupération depuis la base de données

        return entity;
    }

    public List<NotificationDto> toDtoList(List<Notification> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Notification> toEntityList(List<NotificationDto> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}