package com.example.security.ws.converter;

import com.example.security.ws.dto.BlogSectionDTO;
import com.example.security.entity.BlogSection;
import org.springframework.stereotype.Component;

@Component
public class BlogSectionConverter {

    public BlogSection toEntity(BlogSectionDTO dto) {
        BlogSection entity = new BlogSection();
        entity.setId(dto.getId());
        entity.setTitre(dto.getTitre());
        entity.setContenu(dto.getContenu());
        // Note: Le type pourrait être ajouté à l'entité si nécessaire
        return entity;
    }

    public BlogSectionDTO toDTO(BlogSection entity) {
        BlogSectionDTO dto = new BlogSectionDTO();
        dto.setId(entity.getId());
        dto.setTitre(entity.getTitre());
        dto.setContenu(entity.getContenu());
        // Le type peut être déduit du contenu ou défini selon la logique métier
        return dto;
    }
}