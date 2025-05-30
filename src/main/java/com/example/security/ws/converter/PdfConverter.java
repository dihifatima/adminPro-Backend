package com.example.security.ws.converter;

import com.example.security.entity.Creneau;
import com.example.security.entity.DemandeService;
import com.example.security.ws.dto.PdfDto;
import org.springframework.stereotype.Component;

@Component
public class PdfConverter {
    public PdfDto map(DemandeService entity) {
        if (entity == null) {
            return null;
        }

        PdfDto dto = new PdfDto();
        dto.setRef(entity.getRef());
        dto.setDateSoumission(entity.getDateSoumission());

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUserNom(entity.getUser().getFirstname() + " " + entity.getUser().getLastname());
            dto.setEmail(entity.getUser().getEmail());           // Changé
            dto.setTelephone(entity.getUser().getTelephone());   // Changé
        }

        if (entity.getServiceOffert() != null) {
            dto.setServiceOffertId(entity.getServiceOffert().getId());
            dto.setServiceOffertNom(entity.getServiceOffert().getName());        }

        if (entity.getCreneau() != null) {
            dto.setCreneau(String.valueOf(entity.getCreneau().getDateCreneau())); // Changé
            // Information lisible sur le créneau (optionnel)
            Creneau creneau = entity.getCreneau();
            String creneauInfo = String.format("%s de %s à %s",
                    creneau.getDateCreneau(),
                    creneau.getHeureDebut(),
                    creneau.getHeureFin());
        }

        return dto;
    }
}