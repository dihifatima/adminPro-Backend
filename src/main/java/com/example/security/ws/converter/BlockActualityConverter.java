package com.example.security.ws.converter;

import com.example.security.entity.BlockActuality;
import com.example.security.entity.BlogSection;
import com.example.security.ws.dto.BlockActualityDto;
import com.example.security.ws.dto.BlogSectionDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BlockActualityConverter {

    @Autowired
    private BlogSectionConverter sectionConverter;

    public BlockActuality map(BlockActualityDto dto) {
        BlockActuality entity = new BlockActuality();
        if (dto != null) {
            // Copier les propriétés de base (sans les sections)
            BeanUtils.copyProperties(dto, entity, "sections");

            // Conversion manuelle des sections pour éviter la récursion
            if (dto.getSections() != null && !dto.getSections().isEmpty()) {
                Set<BlogSection> sections = new HashSet<>();
                for (BlogSectionDTO sectionDTO : dto.getSections()) {
                    BlogSection section = new BlogSection();
                    section.setId(sectionDTO.getId());
                    section.setTitre(sectionDTO.getTitre());
                    section.setContenu(sectionDTO.getContenu());
                    section.setBlockActuality(entity);
                    sections.add(section);
                }
                entity.setSections(sections);
            }
        }
        return entity;
    }

    public BlockActualityDto map(BlockActuality entity) {
        BlockActualityDto dto = new BlockActualityDto();
        if (entity != null) {
            // Copier les propriétés de base (sans les sections)
            BeanUtils.copyProperties(entity, dto, "sections");

            // Conversion manuelle des sections pour éviter la récursion
            if (entity.getSections() != null && !entity.getSections().isEmpty()) {
                Set<BlogSectionDTO> sectionDTOs = new HashSet<>();
                for (BlogSection section : entity.getSections()) {
                    BlogSectionDTO sectionDTO = new BlogSectionDTO();
                    sectionDTO.setId(section.getId());
                    sectionDTO.setTitre(section.getTitre());
                    sectionDTO.setContenu(section.getContenu());
                    // NE PAS inclure la référence parent pour éviter la récursion
                    sectionDTOs.add(sectionDTO);
                }
                dto.setSections(sectionDTOs);
            }
        }
        return dto;
    }

    public List<BlockActuality> mapListDtos(List<BlockActualityDto> dtos) {
        return dtos.stream().map(this::map).collect(Collectors.toList());
    }

    public List<BlockActualityDto> mapListEntities(List<BlockActuality> entities) {
        return entities.stream().map(this::map).collect(Collectors.toList());
    }
}