package com.example.security.ws.converter;

import com.example.security.entity.Contact;
import com.example.security.ws.dto.ContactDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContactConverter {

    public Contact map(ContactDto dto) {
        Contact entity = new Contact();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    public ContactDto map(Contact entity) {
        ContactDto dto = new ContactDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }

    public List<Contact> mapListDtos(List<ContactDto> dtos) {
        return dtos.stream().map(this::map).collect(Collectors.toList());
    }

    public List<ContactDto> mapListEntities(List<Contact> entities) {
        return entities.stream().map(this::map).collect(Collectors.toList());
    }
}
